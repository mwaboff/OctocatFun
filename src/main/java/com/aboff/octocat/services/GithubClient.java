package com.aboff.octocat.services;

import com.aboff.octocat.models.GithubDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

public interface GithubClient {

    GithubDto getUserDetails(@PathVariable String username) throws IOException;
}
