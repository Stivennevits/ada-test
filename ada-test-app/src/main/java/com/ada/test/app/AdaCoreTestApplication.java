package com.ada.test.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ada.test.*")
@EnableJpaRepositories(basePackages = "com.ada.test.domain.repository")
@EntityScan(basePackages = "com.ada.test.domain.model")
public class AdaCoreTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdaCoreTestApplication.class, args);
	}

}
