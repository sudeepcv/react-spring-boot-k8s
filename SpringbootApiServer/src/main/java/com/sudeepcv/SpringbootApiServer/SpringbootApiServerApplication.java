package com.sudeepcv.SpringbootApiServer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
public class SpringbootApiServerApplication {
	@Autowired
	private UserRepository userRepository;


	public static void main(String[] args) {
		SpringApplication.run(SpringbootApiServerApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Check if the default user already exists by username
		UserTable existingUser = userRepository.findByUsername("JohnDoe");

		if (existingUser == null) {
			// Insert the default user if it doesn't exist
			UserTable defaultUser = new UserTable("JohnDoe", "1234567890");
			userRepository.save(defaultUser);
			System.out.println("Default user inserted: " + defaultUser);
		} else {
			System.out.println("Default user already exists. Skipping insertion.");
		}
	}





}
