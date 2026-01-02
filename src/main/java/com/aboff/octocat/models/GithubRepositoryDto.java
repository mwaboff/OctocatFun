package com.aboff.octocat.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Slf4j
public class GithubRepositoryDto {
    /**
     * Name of repository
     */
    private String name;

    /**
     * URL of repository
     */
    private String url;

    /**
     * Provided a user response from the GitHub hub4j library, parse the user's repositories into desired format.
     *
     * @param userResponse - GHUser - User details from
     * @return List of GithubRepository objects.
     * @throws IOException
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
