package com.aboff.octocat.services;

import com.aboff.octocat.models.GithubDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

/**
 * Client interface for interacting with the GitHub API.
 */
public interface GithubClient {

    /**
     * Retrieves detailed information for a GitHub user.
     *
     * @param username the GitHub username to look up
     * @return a {@link GithubDto} containing user profile and repository
     *         information
     * @throws IOException if an error occurs while communicating with the GitHub
     *                     API
     */
    GithubDto getUserDetails(@PathVariable String username) throws IOException;
}
