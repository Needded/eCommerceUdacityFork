package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	Logger log= LoggerFactory.getLogger(CartController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		log.info("Starting to add item to cart for user: {}", request.getUsername());

		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.warn("User not found: {}", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(item.isEmpty()) {
			log.warn("Item not found: {}", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		log.info("Adding {} units of item {} to cart for user {}", request.getQuantity(), request.getItemId(), request.getUsername());

		IntStream.range(0, request.getQuantity())
				.forEach(i -> cart.addItem(item.get()));

		cartRepository.save(cart);

		log.info("Items successfully added to cart for user {}", request.getUsername());
		return ResponseEntity.ok(cart);
	}
	
	@DeleteMapping("deleteFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		log.info("Starting to remove item from cart for user: {}", request.getUsername());

		User user = userRepository.findByUsername(request.getUsername());
		if (user == null) {
			log.warn("User not found: {}", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());
		if (item.isEmpty()) {
			log.warn("Item not found: {}", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		log.info("Removing {} units of item {} from cart for user {}", request.getQuantity(), request.getItemId(), request.getUsername());

		IntStream.range(0, request.getQuantity())
				.forEach(i -> cart.removeItem(item.get()));

		cartRepository.save(cart);

		log.info("Items successfully removed from cart for user {}", request.getUsername());
		return ResponseEntity.ok(cart);
	}
		
}
