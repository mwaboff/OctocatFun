package com.aboff.octocat.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for GitHub User API using Spring MockMvc.
 * 
 * These tests validate the end-to-end flow from the REST controller
 * through the service layer to the mocked GitHub API client.
 */
@SpringBootTest
@Import(IntegrationTestConfig.class)
@ActiveProfiles("test")
public class GithubApiIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetUserDetails_ValidUsername_ReturnsOk() throws Exception {
        // Load expected response from JSON file
        ClassPathResource resource = new ClassPathResource("karate/fixtures/octocat-expected-response.json");
        String expectedJson = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        mockMvc.perform(get("/api/github/user/octocat")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetUserDetails_InvalidFormat_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/github/user/bad_user_name")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUserDetails_NonExistentUser_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/github/user/nonexistentuser123456")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserDetails_UsernameWithHyphen_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/github/user/test-user")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("test-user"))
                .andExpect(jsonPath("$.display_name").value("Test User"));
    }

    @Test
    void testGetUserDetails_EmptyUsername_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/github/user/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserDetails_UsernameWithSpace_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/github/user/user name")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
