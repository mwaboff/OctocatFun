# Octocat Fun
Octocat Fun provides an easy-to-use REST API endpoint for applications to get formatted GitHub user information. Provide a  GitHub username and the application will do the rest.

## Requirements
- Java 25

## Using the Application
Start the application by following instructions below and then use the below curl to engage the Octocat API.

Please note that, because this application does not authenticate you as a user, there may be limitations in the response due to security limitations or rate limiting.

### Example Request (Local)
```
curl --location '127.0.0.1:8080/api/github/user/octocat' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json'
```

### Example Successful Response
**Response Status:** 200

```json
{
  "avatar": "https://avatars.githubusercontent.com/u/583231?v=4",
  "created_at": "Tue, 25 Jan 2011 18:44:36 GMT",
  "display_name": "The Octocat",
  "email": null,
  "geo_location": "San Francisco",
  "repos": [
    {
      "name": "Hello-World",
      "url": "https://api.github.com/repos/octocat/Hello-World"
    },
    {
      "name": "Spoon-Knife",
      "url": "https://api.github.com/repos/octocat/Spoon-Knife"
    },
    {
      "name": "boysenberry-repo-1",
      "url": "https://api.github.com/repos/octocat/boysenberry-repo-1"
    },
    {
      "name": "git-consortium",
      "url": "https://api.github.com/repos/octocat/git-consortium"
    },
    {
      "name": "hello-worId",
      "url": "https://api.github.com/repos/octocat/hello-worId"
    },
    {
      "name": "linguist",
      "url": "https://api.github.com/repos/octocat/linguist"
    },
    {
      "name": "octocat.github.io",
      "url": "https://api.github.com/repos/octocat/octocat.github.io"
    },
    {
      "name": "test-repo1",
      "url": "https://api.github.com/repos/octocat/test-repo1"
    }
  ],
  "url": "https://api.github.com/users/octocat",
  "user_name": "octocat"
}
```

### Example Failure Response
**Response Status:** 404

```json
{}
```

## How to Run Application Locally
### IntelliJ
1. Import the `.idea/runConfigurations/OctocatApplication.xml` into your Run/Debug Configurations.
2. Click Run.

_Alternatively you can recreate it with the below configuration:_  
**JDK:** `java 25 sdk`   
**Module:** `-cp octocat.main`   
**Spring Boot Class:** `com.aboff.octocat.OctocatApplication`  
**Active Profiles:** `dev`

### Gradlew - Command Line
#### Build Application
```
./gradlew build
```

#### Start Application
```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

#### Run Unit Tests
```
./gradlew test
```

## Miscellaneous

### API Rate Limiting
This application uses the public API for GitHub without the user needing to provide authentication. While this makes  it easier for a small proof-of-concept application, we will be limited by GitHub's rate limiting. As of writing, GitHub limits requests to 60 per hour. More information about GitHub's rate limiting can be [found here](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api?apiVersion=2022-11-28#primary-rate-limit-for-unauthenticated-users).

To mitigate impact of rate limiting, we have implemented caching, so repeated requests to the same account won't contribute to the rate limit.

### GitHub Library vs Feign Client
When deciding how to interact with the GitHub, we had two options: direct API calls or a library. While considering between the two, I decided on using a library for the following reasons:
- Easier to implement
- Easier to test
- Smaller code footprint to maintain, including models, testing, etc

There are a few reasons to choose direct calls using something like Feign Client instead:
- There is no official Java library
- Feign has built in monitoring

Weighing these benefits, I decided for this project, a quick and proven solution such as a library made sense. If we need to expand in the future,
we can update the interface's implementation without changing any other code.

GitHub's documentation provided two suggestions: [hub4j/github-api](https://hub4j.github.io/github-api/) and [jcabi](https://github.jcabi.com/). 
I decided to go with hub4j because it has more usage and continued development.

