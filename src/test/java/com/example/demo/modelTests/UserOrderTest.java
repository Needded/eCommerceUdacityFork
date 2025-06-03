package com.example.demo.modelTests;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserOrderTest {

    @Test
    public void testUserOrderProperties() {
        UserOrder order = new UserOrder();

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        items.add(item);

        User user = new User();
        user.setId(2L);

        order.setId(3L);
        order.setItems(items);
        order.setUser(user);
        order.setTotal(new BigDecimal("19.99"));

        assertEquals(3L, order.getId());
        assertEquals(items, order.getItems());
        assertEquals(user, order.getUser());
        assertEquals(new BigDecimal("19.99"), order.getTotal());
    }

    @Test
    public void testCreateFromCart() {
        Cart cart = new Cart();

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        items.add(item);

        User user = new User();
        user.setId(2L);

        cart.setItems(items);
        cart.setTotal(new BigDecimal("29.99"));
        cart.setUser(user);

        UserOrder order = UserOrder.createFromCart(cart);

        assertNotNull(order);
        assertEquals(items, order.getItems());
        assertEquals(user, order.getUser());
        assertEquals(new BigDecimal("29.99"), order.getTotal());
    }
}

