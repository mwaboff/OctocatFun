package com.aboff.octocat.services;

import com.aboff.octocat.models.GithubDto;
import com.aboff.octocat.services.impl.GithubClientImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHFileNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

    @Mock
    GithubClientImpl githubClient;

    @InjectMocks
    GithubService githubService;

    @Test
    void testGetUserInfo_Success() throws IOException {
        String username = "octocat";
        GithubDto mockDto = GithubDto.builder().user_name(username).created_at(new java.util.Date()).build();

        when(githubClient.getUserDetails(username)).thenReturn(mockDto);

        GithubDto result = githubService.getUserInfo(username);

        assertNotNull(result);
        assertEquals(username, result.getUser_name());
    }

    @Test
    void testGetUserInfo_NotFound() throws IOException {
        String username = "nonexistent";

        when(githubClient.getUserDetails(username)).thenThrow(new GHFileNotFoundException());

        GithubDto result = githubService.getUserInfo(username);

        assertNull(result);
    }

    @Test
    void testGetUserInfo_IOException() throws IOException {
        String username = "error";

        when(githubClient.getUserDetails(username)).thenThrow(new IOException());

        GithubDto result = githubService.getUserInfo(username);

        assertNull(result);
    }
}
