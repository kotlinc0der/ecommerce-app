package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		log.info("Finding user by username '{}'", username);

		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.error("Invalid username. Failed to retrieve user '{}'", username);
			return ResponseEntity.notFound().build();
		}

		log.info("Successfully retrieved user '{}'", username);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		log.info("Creating user '{}'", createUserRequest.getUsername());

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		String password = createUserRequest.getPassword();
		if (password == null || password.length() < 7 || !password.equals(createUserRequest.getConfirmPassword())) {
			log.error("Invalid password. Failed to create user '{}'", user.getUsername());
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);

		log.info("User '{}' was successfully created", user.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
