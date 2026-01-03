package com.aboff.octocat.controllers;

import com.aboff.octocat.models.GithubDto;
import com.aboff.octocat.services.GithubService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for GitHub user and repository information.
 *
 * Provides endpoints to retrieve GitHub user metadata and their public
 * repositories.
 * Input validation is enforced via Jakarta Bean Validation annotations.
 */
@RestController
@RequestMapping(path = "/api/github")
@Slf4j
@Validated
public class GithubController {

    @Autowired
    private GithubService githubService;

    /**
     * Retrieves GitHub user profile and repository metadata.
     * 
     * This endpoint fetches user information from the GitHub API via the
     * {@link GithubService}.
     * The username is validated for format compliance with GitHub's naming rules:
     * alphanumeric characters and single hyphens only, cannot begin or end with a
     * hyphen.
     *
     * @param username the GitHub username to look up; must not be null, blank, or
     *                 contain invalid characters
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *         <li>{@code 200 OK} with {@link GithubDto} body if user is found</li>
     *         <li>{@code 400 Bad Request} if the username format is invalid</li>
     *         <li>{@code 404 Not Found} if the user does not exist on GitHub</li>
     *         <li>{@code 406 Not Acceptable} if the client requests an unsupported content type</li>
     *         </ul>
     * @throws jakarta.validation.ConstraintViolationException if username is null
     *                                                         or blank
     */
    @GetMapping(path = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GithubDto> getUserDetails(@PathVariable @NotNull @NotBlank String username) {
        log.info("Request received for GitHub user: {}", username);

        // Check if username matches GitHub's naming requirements to help avoid
        // malicious user input. When signing up you are given these instructions:
        // "Username may only contain alphanumeric characters or single hyphens, and
        // cannot begin or end with a hyphen."
        if (!username.matches("^[a-zA-Z0-9]+(-[a-zA-Z0-9]+)*$")) {
            log.error("Requested user is not a valid name.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        GithubDto userDetails = githubService.getUserInfo(username);

        if (userDetails == null) {
            log.info("No data found for username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("Successfully found for user: {}", username);
        return ResponseEntity.ok(userDetails);

    }
}
