package com.aboff.octocat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Main entry point for the Octocat Spring Boot application.
 *
 * @author Michael Aboff
 */
@SpringBootApplication
@EnableRetry
public class OctocatApplication {

	/**
	 * Application entry point.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(OctocatApplication.class, args);
	}

}
