package com.aboff.octocat.models;

import lombok.Builder;
import lombok.Data;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
     * Timestamp for found GitHub user's creation time in GMT
     */
    private Date created_at;

    /**
     * List of repositories associated with the found GitHub user.
     */
    private List<GithubRepositoryDto> repos;

    /**
     * Returns the user's account creation timestamp formatted in UTC.
     *
     * The format used is: "EEE, dd MMM yyyy HH:mm:ss z" (e.g., "Tue, 25 Jan 2011
     * 18:44:36 Z").
     * 
     * @return the formatted creation timestamp string in UTC timezone
     */
    public String getCreated_at() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")
                .withZone(ZoneOffset.UTC);

        ZonedDateTime utcInstant = this.created_at.toInstant().atZone(ZoneOffset.UTC);
        return utcInstant.format(formatter);
    }

    /**
     * Constructs a {@link GithubDto} from a GitHub API user response.
     * 
     * Maps user profile fields to build the final response to the user.
     *
     * @param userResponse the {@link GHUser} object from the GitHub hub4j library
     * @return a fully populated {@link GithubDto} instance
     * @throws IOException if an error occurs while fetching user data or
     *                     repositories from GitHub
     */
    public static GithubDto buildFromUser(GHUser userResponse) throws IOException {
        return GithubDto.builder()
                .user_name(userResponse.getLogin())
                .display_name(userResponse.getName())
                .avatar(userResponse.getAvatarUrl())
                .geo_location(userResponse.getLocation())
                .email(userResponse.getEmail())
                .url(String.valueOf(userResponse.getUrl()))
                .created_at(userResponse.getCreatedAt())
                .repos(GithubRepositoryDto.buildListFromUser(userResponse))
                .build();
    }

}
