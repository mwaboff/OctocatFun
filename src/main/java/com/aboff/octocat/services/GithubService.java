package com.aboff.octocat.services;

import com.aboff.octocat.models.GithubDto;
import com.aboff.octocat.services.impl.GithubClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class GithubService {

    @Autowired
    GithubClientImpl githubClient;

    public GithubDto getUserInfo(String username) {
        try {
            return githubClient.getUserDetails(username);
        } catch (GHFileNotFoundException e) {
            log.error("Not Found", e);
            return null;
        } catch (IOException e) {
            log.error("Error when fetching details.", e);
            return null;
        }
    }
}
