package com.example.demo.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;


@RestController
@RequestMapping("/api/user")
public class UserController {

	private Logger log = LoggerFactory.getLogger(UserController.class);
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UserRepository userRepository;
	private CartRepository cartRepository;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, CartRepository cartRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
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
		log.info("CreateUser request received for username '{}'", createUserRequest.getUsername());

		try {
			User user = new User();
			user.setUsername(createUserRequest.getUsername());
			Cart cart = new Cart();
			cartRepository.save(cart);
			user.setCart(cart);

			String password = createUserRequest.getPassword();
			if (password == null || password.length() < 7 || !password.equals(createUserRequest.getConfirmPassword())) {
				log.error("CreateUser request failed: Invalid password for username '{}'", user.getUsername());
				return ResponseEntity.badRequest().build();
			}

			user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
			userRepository.save(user);

			log.info("CreateUser request successful for username '{}'", user.getUsername());
			return ResponseEntity.ok(user);

		} catch (Exception e) {
			log.error("Exception occurred while creating user '{}': {}", createUserRequest.getUsername(), e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
