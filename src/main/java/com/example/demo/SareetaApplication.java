package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
@EntityScan("com.example.demo.model.persistence")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SareetaApplication {
	private static final String ADMIN = "admin";
	private static final String PASSWORD = "password";

	public static void main(String[] args) {
		SpringApplication.run(SareetaApplication.class, args);
	}

	@Bean
	CommandLineRunner createDefaultUser(UserRepository userRepository) {
		return args  -> {
			User user = new User();
			user.setUsername(ADMIN);
			user.setPassword(bCryptPasswordEncoder().encode(PASSWORD));
			user.setCart(new Cart());
			userRepository.save(user);
		};
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
