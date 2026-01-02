package com.aboff.octocat.services.impl;

import com.aboff.octocat.models.GithubDto;
import com.aboff.octocat.services.GithubClient;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Implementation of {@link GithubClient} using the hub4j GitHub API library.
 * 
 * This client connects to GitHub anonymously and fetches user information.
 * Results are cached to improve performance for repeated requests.
 * Retries are automatically performed on transient I/O failures.
 */
@Component
@Slf4j
public class GithubClientImpl implements GithubClient {

    /**
     * The GitHub API client instance. Lazily initialized on first use.
     */
    GitHub github;

    /**
     * Retrieves detailed information for a GitHub user.
     * 
     * Lazily initializes an anonymous GitHub API connection on first invocation.
     * The results are cached under the "github-user" cache name.
     * Transient {@link IOException} failures trigger automatic retries.
     *
     * @param username the GitHub username to look up
     * @return a {@link GithubDto} containing user profile and repository
     *         information
     * @throws IOException if an error occurs while communicating with GitHub after
     *                     all retries
     */
    @Override
    @Cacheable("github-user")
    @Retryable(includes = IOException.class, delay = 100, jitter = 10)
    public GithubDto getUserDetails(String username) throws IOException {
        if (this.github == null) {
            log.info("Initializing new anonymous GitHub client...");
            this.github = GitHub.connectAnonymously();
            log.info("GitHub created successfully");
        }

        log.info("Requesting user data...");
        GHUser userResponse = this.github.getUser(username);
        log.info("Found user data for {}", username);
        return GithubDto.buildFromUser(userResponse);
    }
}
