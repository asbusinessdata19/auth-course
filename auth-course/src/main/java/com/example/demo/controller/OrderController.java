package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.entity.UserOrder;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public OrderRepository orderRepository;


	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("Error Submitting Order for user "+username+ ": username couldn't be found");
			return ResponseEntity.notFound().build();
		}

		UserOrder order = new UserOrder();
		order.setTotal(user.getCart().getTotal());
		order.setItems(user.getCart().getItems());
		order.setUser(user);
		order.equals(order);
		System.out.println(order.getTotal());
		System.out.println(order.getItems());
		orderRepository.save(order);
		log.info("user "+username+ " order has been submitted succesfully");
		return ResponseEntity.ok(order);
	}

	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
