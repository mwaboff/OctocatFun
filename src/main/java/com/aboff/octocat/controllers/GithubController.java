package com.aboff.octocat.controllers;

import com.aboff.octocat.models.GithubDto;
import com.aboff.octocat.services.GithubService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/github")
@Slf4j
@Validated
public class GithubController {

    @Autowired
    private GithubService githubService;
    /**
     * Get GitHub user and repository metadata when available.
     *
     * @param username
     * @return GithubResponseDto object containing user and repository information.
     */
    @GetMapping(path="/user/{username}")
    ResponseEntity<GithubDto> getUserDetails(@PathVariable @NotNull @NotBlank String username) {
        log.info("Request received for GitHub user: {}", username);
        GithubDto userDetails = githubService.getUserInfo(username);

        if (userDetails == null) {
            log.info("No data found for username: {}", username);
            return ResponseEntity.status(404).build();
        }

        log.info("Found data for user: {}", String.valueOf(userDetails));
        return ResponseEntity.ok(userDetails);

    }
}
