package com.aboff.octocat.services.impl;

import com.aboff.octocat.models.GithubDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GithubClientImplTest {

    @Mock
    GitHub github;

    @Mock
    GHUser ghUser;

    @InjectMocks
    GithubClientImpl githubClient;

    @Test
    void testGetUserDetails() throws IOException {

        String username = "octocat";

        when(github.getUser(username)).thenReturn(ghUser);
        when(ghUser.getLogin()).thenReturn(username);
        when(ghUser.getPublicRepoCount()).thenReturn(0);

        // Mocking an empty map for repositories
        doReturn(java.util.Collections.emptyMap()).when(ghUser).getRepositories();

        GithubDto result = githubClient.getUserDetails(username);

        assertNotNull(result);
        assertEquals(username, result.getUser_name());
        verify(github).getUser(username);
    }

    @Test
    void testGetUserDetails_InitializesGithubWhenNull() throws IOException {
        String username = "octocat";

        GithubClientImpl spyClient = spy(new GithubClientImpl());

        try (var mockedStatic = mockStatic(GitHub.class)) {
            mockedStatic.when(GitHub::connectAnonymously).thenReturn(github);

            when(github.getUser(username)).thenReturn(ghUser);
            when(ghUser.getLogin()).thenReturn(username);
            when(ghUser.getPublicRepoCount()).thenReturn(0);
            doReturn(java.util.Collections.emptyMap()).when(ghUser).getRepositories();

            GithubDto result = spyClient.getUserDetails(username);

            assertNotNull(result);
            mockedStatic.verify(GitHub::connectAnonymously, times(1));
        }
    }

    @Test
    void testGetUserDetails_DoesNotReinitializeGithubWhenAlreadySet() throws IOException {
        String username = "octocat";

        githubClient.github = github;

        when(github.getUser(username)).thenReturn(ghUser);
        when(ghUser.getLogin()).thenReturn(username);
        when(ghUser.getPublicRepoCount()).thenReturn(0);
        doReturn(java.util.Collections.emptyMap()).when(ghUser).getRepositories();

        try (var mockedStatic = mockStatic(GitHub.class)) {
            GithubDto result = githubClient.getUserDetails(username);
            assertNotNull(result);
            mockedStatic.verify(GitHub::connectAnonymously, never());
        }
    }
}
