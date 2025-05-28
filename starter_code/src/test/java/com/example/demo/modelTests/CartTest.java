package com.example.demo.modelTests;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartTest {

    @Test
    public void testCartProperties() {
        Cart cart = new Cart();
        cart.setId(1L);

        Item item1 = new Item();
        item1.setId(100L);
        Item item2 = new Item();
        item2.setId(101L);
        List<Item> items = Arrays.asList(item1, item2);
        cart.setItems(items);

        User user = new User();
        user.setId(10L);
        cart.setUser(user);

        cart.setTotal(new BigDecimal("29.99"));

        assertEquals(1L, cart.getId());
        assertEquals(items, cart.getItems());
        assertEquals(user, cart.getUser());
        assertEquals(new BigDecimal("29.99"), cart.getTotal());
    }
}

