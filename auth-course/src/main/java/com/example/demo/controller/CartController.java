package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.ModifyCartRequest;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public CartRepository cartRepository;

	@Autowired
	public ItemRepository itemRepository;

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		if(cart ==null) {
			cart = new Cart();
			cart.setUser(user);
			List<Item> items = new ArrayList<Item>();
			cart.setItems(items);
		}
		for(int i =0;i<=request.getQuantity();i++) {
			cart.addItem(item.get());
		}
		cartRepository.save(cart);
		if(cart!=null) {
			System.out.println(cart);
		}
		user.setCart(cart);
		userRepository.save(user);
		return ResponseEntity.ok(cart);
	}

	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
		.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		return ResponseEntity.ok(cart);
	}

}
