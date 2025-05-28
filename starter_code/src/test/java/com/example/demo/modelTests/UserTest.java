package com.example.demo.modelTests;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    public void testUserProperties() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("securePassword");

        Cart cart = new Cart();
        cart.setId(10L);
        user.setCart(cart);

        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("securePassword", user.getPassword());
        assertEquals(cart, user.getCart());
    }
}

