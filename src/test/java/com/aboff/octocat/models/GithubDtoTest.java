package com.aboff.octocat.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubDtoTest {

    @Mock
    GHUser ghUser;

    @Mock
    GHRepository ghRepository;

    @Test
    void testGetCreatedAt() {
        // Thursday, June 02, 2022 12:00:00 PM UTC
        java.util.Date fixedDate = new java.util.Date(1654171200000L);

        GithubDto dto = GithubDto.builder()
                .user_name("test-user")
                .created_at(fixedDate)
                .build();

        // Expected format: EEE, dd MMM yyyy HH:mm:ss z
        String expected = "Thu, 02 Jun 2022 12:00:00 Z";
        assertEquals(expected, dto.getCreated_at());
    }


    @Test
    void testBuildFromUser() throws IOException {
        // Arrange
        String login = "octocat";
        String name = "The Octocat";
        String avatarUrl = "http://example.com/avatar";
        String location = "San Francisco";
        String email = "octocat@github.com";
        Date createdAt = new java.util.Date(1654171200000L);
        String expectedCreatedAt = "Thu, 02 Jun 2022 12:00:00 Z";
        String repoName = "Hello-World";

        when(ghUser.getLogin()).thenReturn(login);
        when(ghUser.getName()).thenReturn(name);
        when(ghUser.getAvatarUrl()).thenReturn(avatarUrl);
        when(ghUser.getLocation()).thenReturn(location);
        when(ghUser.getEmail()).thenReturn(email);
        when(ghUser.getCreatedAt()).thenReturn(createdAt);
        when(ghUser.getPublicRepoCount()).thenReturn(2);
        when(ghRepository.getName()).thenReturn(repoName);
        when(ghRepository.getUrl()).thenReturn(null);
        doReturn(Map.of(repoName, ghRepository, "Second-Repo", ghRepository)).when(ghUser).getRepositories();

        // Act
        GithubDto dto = GithubDto.buildFromUser(ghUser);

        // Assert
        assertEquals(login, dto.getUser_name());
        assertEquals(name, dto.getDisplay_name());
        assertEquals(avatarUrl, dto.getAvatar());
        assertEquals(location, dto.getGeo_location());
        assertEquals(email, dto.getEmail());
        assertEquals("null", dto.getUrl());
        assertEquals(expectedCreatedAt, dto.getCreated_at());
        assertNotNull(dto.getRepos());
        assertEquals(2, dto.getRepos().size());
        assertEquals(repoName, dto.getRepos().getFirst().getName());
        assertEquals("null", dto.getRepos().getFirst().getUrl());
    }

}
