package com.aboff.octocat.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryDtoTest {

    @Mock
    GHUser ghUser;

    @Mock
    GHRepository ghRepository;

    @Test
    void testBuildListFromUser() throws IOException {
        // Arrange
        when(ghUser.getPublicRepoCount()).thenReturn(1);
        when(ghRepository.getName()).thenReturn("repo1");
        // doReturn(java.net.URI.create("http://github.com/user/repo1").toURL()).when(ghRepository).getUrl();
        doReturn(Map.of("repo1", ghRepository)).when(ghUser).getRepositories();

        // Act
        List<GithubRepositoryDto> result = GithubRepositoryDto.buildListFromUser(ghUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("repo1", result.getFirst().getName());
        assertEquals("null", result.getFirst().getUrl());
    }
}
