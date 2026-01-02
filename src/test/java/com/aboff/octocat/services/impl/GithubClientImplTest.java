package com.aboff.octocat.services.impl;

import com.aboff.octocat.models.GithubDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubClientImplTest {

    @Mock
    GitHub github;

    @Mock
    GHUser ghUser;

    @InjectMocks
    GithubClientImpl githubClient;

    @Test
    void testGetUserDetails() throws IOException {
        // Since 'github' field is package-private/private, we can use
        // ReflectionTestUtils or if it was autowired, InjectMocks helps.
        // However, the original code lazily initializes it. We want to inject the mock.
        // InjectMocks should place 'github' mock into 'githubClient'.

        // Wait, the original code initializes github if null.
        // "if (this.github == null) { this.github = GitHub.connectAnonymously(); }"
        // So if we inject a mock into 'this.github', it won't be null, and it will use
        // the mock. Good.

        String username = "octocat";

        when(github.getUser(username)).thenReturn(ghUser);
        when(ghUser.getLogin()).thenReturn(username);
        when(ghUser.getPublicRepoCount()).thenReturn(0);
        // If we don't mock getRepositories(), buildFromUser might fail if it iterates.
        // buildFromUser mocks. Let's look at buildFromUser again.
        // It calls GithubRepositoryDto.buildListFromUser(userResponse)
        // which iterates userResponse.getRepositories().
        // So we need to mock that too.

        // Mocking an empty map for repositories
        doReturn(java.util.Collections.emptyMap()).when(ghUser).getRepositories();

        GithubDto result = githubClient.getUserDetails(username);

        assertNotNull(result);
        assertEquals(username, result.getUser_name());
        verify(github).getUser(username);
    }
}
