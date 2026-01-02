package com.aboff.octocat.services;

import com.aboff.octocat.models.GithubDto;
import com.aboff.octocat.services.impl.GithubClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service layer for GitHub user operations.
 */
@Slf4j
@Service
public class GithubService {

    @Autowired
    GithubClientImpl githubClient;

    /**
     * Retrieves GitHub user information by username.
     * 
     * Delegates to {@link GithubClientImpl} to fetch user data from the GitHub API.
     * Handles exceptions gracefully by logging errors and returning {@code null}.
     *
     * @param username the GitHub username to look up
     * @return a {@link GithubDto} containing user information, or {@code null} if:
     *         <ul>
     *         <li>The user was not found on GitHub
     *         ({@link GHFileNotFoundException})</li>
     *         <li>An I/O error occurred during the request
     *         ({@link IOException})</li>
     *         </ul>
     */
    public GithubDto getUserInfo(String username) {
        try {
            return githubClient.getUserDetails(username);
        } catch (GHFileNotFoundException e) {
            log.error("Not Found", e);
            return null;
        } catch (IOException e) {
            log.error("Error when fetching details.", e);
            return null;
        }
    }
}
