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

@Component
@Slf4j
public class GithubClientImpl implements GithubClient {

    GitHub github;

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
