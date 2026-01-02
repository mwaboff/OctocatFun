package com.aboff.octocat.models;

import lombok.Builder;
import lombok.Data;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.util.List;

/**
 * DTO response combining user and repository API calls.
 */
@Builder
@Data
public class GithubDto {

    /**
     * Username of found GitHub user
     */
    private String user_name;

    /**
     * Display name of found GitHub user
     */
    private String display_name;

    /**
     * Avatar URL for found GitHub user
     */
    private String avatar;

    /**
     * Geographic Location for found GitHub user
     */
    private String geo_location;

    /**
     * Email for found GitHub user
     */
    private String email;

    /**
     * URL for found GitHub user
     */
    private String url;

    /**
     * GMT Timestamp for found GitHub user's creation time. Example format: Tue, 25 Jan 2011 18:44:36 GMT
     */
    private String created_at;

    /**
     * List of repositories associated with the found GitHub user.
     */
    private List<GithubRepositoryDto> repos;

    public static GithubDto buildFromUser(GHUser userResponse) throws IOException {
        return GithubDto.builder()
                .user_name(userResponse.getLogin())
                .display_name(userResponse.getName())
                .avatar(userResponse.getAvatarUrl())
                .geo_location(userResponse.getLocation())
                .email(userResponse.getEmail())
                .url(String.valueOf(userResponse.getUrl()))
                .created_at(String.valueOf(userResponse.getCreatedAt()))
                .repos(GithubRepositoryDto.buildListFromUser(userResponse))
                .build();
    }

}
