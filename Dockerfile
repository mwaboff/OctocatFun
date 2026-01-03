# Multi-stage build for Spring Boot application

# Stage 1: Build the application
FROM gradle:9.2.1-jdk25 AS build
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src/ src/

# Build the Spring Boot executable JAR (skip tests for faster builds)
# Use bootJar to ensure we get the executable JAR, not the plain JAR
RUN ./gradlew bootJar -x test

# Stage 2: Runtime image
# Note: If Java 25 is not available, use: eclipse-temurin:23-jre-alpine or eclipse-temurin:21-jre-alpine
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the Spring Boot executable JAR from build stage
# The pattern matches the boot JAR (not the plain JAR)
COPY --from=build /app/build/libs/octocat-*.jar app.jar

# Expose the application port
EXPOSE 8080

# Health check (optional - remove if actuator is not available)
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

