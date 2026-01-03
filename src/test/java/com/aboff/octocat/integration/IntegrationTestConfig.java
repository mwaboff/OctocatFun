package com.aboff.octocat.integration;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GHFileNotFoundException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test configuration for integration tests.
 * Provides mocked GitHub API beans to avoid real API calls during testing.
 */
@TestConfiguration
public class IntegrationTestConfig {

    /**
     * Creates a mocked GitHub client bean for integration testing.
     * 
     * @return a mocked GitHub instance with stubbed responses
     * @throws IOException if an error occurs during mocking setup
     */
    @Bean
    @Primary
    public GitHub mockGitHub() throws IOException {
        GitHub github = mock(GitHub.class);

        GHUser octocatUser = mock(GHUser.class);
        when(octocatUser.getLogin()).thenReturn("octocat");
        when(octocatUser.getName()).thenReturn("The Octocat");
        when(octocatUser.getAvatarUrl()).thenReturn("https://avatars.githubusercontent.com/u/583231");
        when(octocatUser.getLocation()).thenReturn("San Francisco");
        when(octocatUser.getEmail()).thenReturn("octocat@github.com");
        when(octocatUser.getCreatedAt()).thenReturn(new Date(1654171200000L)); // Thu, 02 Jun 2022 12:00:00 Z
        when(octocatUser.getPublicRepoCount()).thenReturn(1);

        GHRepository helloWorldRepo = mock(GHRepository.class);
        when(helloWorldRepo.getName()).thenReturn("Hello-World");
        doReturn(Map.of("Hello-World", helloWorldRepo)).when(octocatUser).getRepositories();

        when(github.getUser("octocat")).thenReturn(octocatUser);

        // Mock for "test-user" (valid format, exists)
        GHUser testUser = mock(GHUser.class);
        when(testUser.getLogin()).thenReturn("test-user");
        when(testUser.getName()).thenReturn("Test User");
        when(testUser.getAvatarUrl()).thenReturn("https://avatars.githubusercontent.com/u/1");
        when(testUser.getLocation()).thenReturn("Test Location");
        when(testUser.getEmail()).thenReturn("test@example.com");
        when(testUser.getCreatedAt()).thenReturn(new Date(1654171200000L));
        when(testUser.getPublicRepoCount()).thenReturn(0);
        doReturn(Map.of()).when(testUser).getRepositories();

        when(github.getUser("test-user")).thenReturn(testUser);

        // Mock for non-existent users - throw GHFileNotFoundException
        when(github.getUser("nonexistentuser123456"))
                .thenThrow(new GHFileNotFoundException("User not found"));

        return github;
    }
}
