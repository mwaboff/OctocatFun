package com.aboff.octocat.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO representing a GitHub repository.
 */
@Builder
@Data
@Slf4j
public class GithubRepositoryDto {
    /**
     * Name of the repository
     */
    private String name;

    /**
     * API URL of the repository.
     */
    private String url;

    /**
     * Builds a list of {@link GithubRepositoryDto} objects from a GitHub user's
     * repositories.
     * 
     * Iterates through all public repositories associated with the user and maps
     * each to a DTO containing the repository name and URL.
     *
     * @param userResponse the {@link GHUser} object containing repository
     *                     information
     * @return a list of {@link GithubRepositoryDto} objects; empty list if user has
     *         no repositories
     * @throws IOException if an error occurs while fetching repository data from
     *                     GitHub
     */
    public static List<GithubRepositoryDto> buildListFromUser(GHUser userResponse) throws IOException {
        List<GithubRepositoryDto> result = new ArrayList<>();
        log.info("Building list of repositories. Total Public Repos: {}", userResponse.getPublicRepoCount());
        userResponse.getRepositories().forEach((key, value) -> {
            result.add(GithubRepositoryDto.builder()
                    .name(value.getName())
                    .url(String.valueOf(value.getUrl()))
                    .build());
        });
        return result;
    }
}
