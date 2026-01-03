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

### Error Response Statuses
**Error Response Status:**
- 400 - Bad Request (possibly caused by malformed username)
- 404 - Not Found (possibly caused by user not found)

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

#### Run Integration Tests

## Miscellaneous

### API Rate Limiting
This application uses the public API for GitHub without the user needing to provide authentication. While this makes it easier for a small proof-of-concept application, we will be limited by GitHub's rate limiting. As of writing, GitHub limits requests to 60 per hour. More information about GitHub's rate limiting can be [found here](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api?apiVersion=2022-11-28#primary-rate-limit-for-unauthenticated-users).

To mitigate impact of rate limiting, we have implemented caching, so repeated requests to the same account won't contribute to the rate limit. But note that this may cause a delay in this application receiving recent changes.

## Design Decisions
Below I will discuss why I decided on some of the decisions I made during this project. While there may have been better choices, I hope my reasoning clarifies why I settled what I did.

### Connecting to GitHub: Library vs API

When connecting to an API, we have the choice of either directly calling the API endpoints or using a pre-built library (when available). While GitHub does not offer an official Java library, it does [recommend two community made ones](https://docs.github.com/en/rest/using-the-rest-api/libraries-for-the-rest-api?apiVersion=2022-11-28#java) that are actively developed.

There are pros and cons for choosing either strategy:

- Libraries are often quicker to implement, have pre-built models, and are thoroughly tested. Unofficial libraries might not support niche use cases, be slow to update, and may lose support in the future.

- Direct API calls give us greater flexibility and a guarantee that we can use the latest API features. Specific API clients, such as Feign, also offer robust feature sets (including built in monitoring capabilities) that make complex development easier. However, compared to a pre-built library, there is more to develop, maintain, and test.

Because this project is a proof of concept with a simple use case and a quick development time (~ 5 hours), I believe the benefits of using a library outweighs its cons. If this were a more long term or more complex use-case, I would more heavily consider a more custom Feign client based API solution.

GitHub's documentation provided two suggestions: [hub4j/github-api](https://hub4j.github.io/github-api/) and [jcabi](https://github.jcabi.com/). 
I decided to go with hub4j because it has more usage, GitHub stars, recent development, and [a permissive MIT license that allows commercial use](https://github.com/hub4j/github-api/blob/c57be3c8223639984ac9f95995efef853ba079f4/LICENSE.txt).

### Created_At Timezone
The Coding Exercise documentation requests that we present the user's `created_at` date in a particular format `Tue, 25 Jan 2011 18:44:36 GMT`. I asked for clarity if a specific timezone was desired, the response was "The date-time value for that field does not need to be normalized to one specific time zone."

Even though normalizing to a timezone was not a requirement, not doing so makes it difficult when running tests in different environments and can lead to confusion when consumed by a client application. Depending on what the timezone of the machine running the server is, you can get different results. Therefore, I decided to set the provided timezone to a standard UTC, which would make it easy for a client to modify with an offset as desired.

### Caching
As noted above in the `Miscellaneous / API Rate Limiting` discussion above, we are using Caching to reduce the impact of GitHub's 60 requests/hour rate limiting. A preferred alternative would be to set up an API access key saved as an environment variable that could be set on the server to allow a higher limit in addition to Caching with a relatively low time to live. For this proof of concept, using caching.

In this proof of concept, we are not defining a specific cache provider so Spring Boot defaults to the [Simple provider](https://docs.spring.io/spring-boot/reference/io/caching.html#io.caching.provider.simple). Spring Boot's documentation indicates this is not preferred for production environments. Given more time, I would set up and test another provider such as [JCache](https://docs.spring.io/spring-boot/reference/io/caching.html#io.caching.provider.jcache) or [Hazelcast](https://docs.spring.io/spring-boot/reference/io/caching.html#io.caching.provider.hazelcast).

The Spring Boot documentation also suggests disabling caching during unit tests. To accommodate this, I created a separate test profile that sets the provider to none as recommended by the documentation.

### Retries
Making calls to external APIs always carry a risk of a blip in availability. To add a little extra resiliency, I like adding retries with a backoff delay and jitter. In this case, I am excluding a GHFileNotFoundException as we shouldn't retry if the user does not exist.

I enabled the random backoff flag as an easy way to introduce jitter, to help avoid the "thundering herd" problem.

### Ensuring Valid User Data
Whenever using user provided input, we should always validate the data given. To do this I implemented the following:
1. @NotNull and @NotBlank validation annotations. These will check to make sure we're given a valid string, if not it will automatically return an invalid request error.
2. Manual regex checks to validate that the username provided matches GitHub's own username requirements ("Username may only contain alphanumeric characters or single hyphens, and cannot begin or end with a hyphen.").

Because we're not saving to a database, there's limited injection opportunities so the above should provide reasonable protection from malicious actors.

### AI Usage
Because of the time constraints for the project and because I believe AI is an incredibly powerful tool that engineers need to master, I used AI to help write the following:
1. Javadocs for each method
2. Unit tests
3. Integration tests
4. Troubleshooting initial Gradle configuration mistakes

For each of the above, I reviewed and touched every line to ensure it matched my style so I could ensure I stood by the work.

I did not use AI to build the logic or make any design decisions for the application. 