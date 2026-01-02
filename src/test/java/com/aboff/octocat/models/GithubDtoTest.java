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
    void testBuildFromUser() throws IOException {
        // Arrange
        String login = "octocat";
        String name = "The Octocat";
        String avatarUrl = "http://example.com/avatar";
        String location = "San Francisco";
        String email = "octocat@github.com";
        String htmlUrl = "http://github.com/octocat";
        Date createdAt = new Date();

        when(ghUser.getLogin()).thenReturn(login);
        when(ghUser.getName()).thenReturn(name);
        when(ghUser.getAvatarUrl()).thenReturn(avatarUrl);
        when(ghUser.getLocation()).thenReturn(location);
        when(ghUser.getEmail()).thenReturn(email);
        // doReturn(java.net.URI.create(htmlUrl).toURL()).when(ghUser).getUrl();
        when(ghUser.getCreatedAt()).thenReturn(createdAt);

        // Mock repositories
        when(ghUser.getPublicRepoCount()).thenReturn(1);
        when(ghRepository.getName()).thenReturn("Hello-World");
        // doReturn(java.net.URI.create("http://github.com/octocat/Hello-World").toURL()).when(ghRepository).getUrl();
        doReturn(Map.of("Hello-World", ghRepository)).when(ghUser).getRepositories();

        // Act
        GithubDto dto = GithubDto.buildFromUser(ghUser);

        // Assert
        assertEquals(login, dto.getUser_name());
        assertEquals(name, dto.getDisplay_name());
        assertEquals(avatarUrl, dto.getAvatar());
        assertEquals(location, dto.getGeo_location());
        assertEquals(email, dto.getEmail());
        assertEquals("null", dto.getUrl());
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                .ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")
                .withZone(java.time.ZoneId.of("Etc/GMT"));
        java.time.ZonedDateTime utcInstant = createdAt.toInstant().atZone(java.time.ZoneId.of("Etc/GMT"));
        assertEquals(utcInstant.format(formatter), dto.getCreated_at());
        assertNotNull(dto.getRepos());
        assertEquals(1, dto.getRepos().size());
        assertEquals("Hello-World", dto.getRepos().get(0).getName());
        assertEquals("null", dto.getRepos().get(0).getUrl());
    }

    @Test
    void testLombokMethods() {
        GithubDto dto1 = GithubDto.builder().user_name("user1").created_at(new Date()).build();
        GithubDto dto2 = GithubDto.builder().user_name("user1").created_at(new Date()).build();
        GithubDto dto3 = GithubDto.builder().user_name("user2").created_at(new Date()).build();

        // Equals and HashCode
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        // ToString
        assertNotNull(dto1.toString());

        // Getters and Setters
        dto1.setDisplay_name("Name");
        assertEquals("Name", dto1.getDisplay_name());
    }

}
