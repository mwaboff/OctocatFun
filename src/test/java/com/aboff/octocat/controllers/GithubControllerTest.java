package com.aboff.octocat.controllers;

import com.aboff.octocat.models.GithubDto;
import com.aboff.octocat.services.GithubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class GithubControllerTest {

    @Autowired
    GithubController githubController;

    @MockitoBean
    GithubService githubService;

    @Test
    void testGetUserDetails_Success() {
        String username = "octocat";
        GithubDto mockDto = GithubDto.builder().user_name(username).created_at(new java.util.Date()).build();

        when(githubService.getUserInfo(username)).thenReturn(mockDto);

        ResponseEntity<GithubDto> response = githubController.getUserDetails(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(username, response.getBody().getUser_name());
    }

    @Test
    void testGetUserDetails_PatternMismatch() {
        ResponseEntity<GithubDto> response = githubController.getUserDetails("bad_user_name");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetUserDetails_NotFound() {
        String username = "nonexistent";

        when(githubService.getUserInfo(username)).thenReturn(null);

        ResponseEntity<GithubDto> response = githubController.getUserDetails(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetUserDetails_NullUsername() {
        org.junit.jupiter.api.Assertions.assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            githubController.getUserDetails(null);
        });
    }

    @Test
    void testGetUserDetails_EmptyUsername() {
        org.junit.jupiter.api.Assertions.assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            githubController.getUserDetails("");
        });
    }

    @Test
    void testGetUserDetails_BlankUsername() {
        org.junit.jupiter.api.Assertions.assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            githubController.getUserDetails(" ");
        });
    }

    @Test
    void testGetUserDetails_PatternMatch_Complex() {
        String username = "octo-cat-123";
        GithubDto mockDto = GithubDto.builder().user_name(username).created_at(new java.util.Date()).build();
        when(githubService.getUserInfo(username)).thenReturn(mockDto);

        ResponseEntity<GithubDto> response = githubController.getUserDetails(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(username, response.getBody().getUser_name());
    }
}
