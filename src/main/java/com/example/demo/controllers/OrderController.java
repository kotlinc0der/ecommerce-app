package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private Logger log = LoggerFactory.getLogger(OrderController.class);

	private final UserRepository userRepository;
	private final OrderRepository orderRepository;

	public OrderController(UserRepository userRepository, OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		log.info("Submitting order for user '{}'", username);

		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("Invalid username. Failed to submit order for user '{}'", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);

		log.info("Order was successfully submitted for user '{}'", username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		log.info("Getting orders for user '{}'", username);

		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("Invalid username. Failed to retrieve orders for user '{}'", username);
			return ResponseEntity.notFound().build();
		}

		log.info("Orders successfully retrieved for user '{}'", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
