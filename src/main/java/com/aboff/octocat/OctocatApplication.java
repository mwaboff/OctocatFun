package com.aboff.octocat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OctocatApplication {

	public static void main(String[] args) {
		SpringApplication.run(OctocatApplication.class, args);
	}

}
