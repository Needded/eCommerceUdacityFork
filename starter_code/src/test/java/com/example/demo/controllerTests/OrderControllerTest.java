package com.example.demo.controllerTests;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.controllers.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private OrderRepository orderRepo;

    private User user;
    private Item item;
    private Cart cart;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setId(0L);

        item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round.");
        item.setPrice(new BigDecimal("2.99"));

        cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("2.99"));
        cart.setUser(user);
        user.setCart(cart);
    }

    @Test
    public void testSubmit() {
        when(userRepo.findByUsername("test")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertNotNull(order);
        assertNotNull(order.getItems());
        assertNotNull(order.getTotal());
        assertNotNull(order.getUser());
    }

    @Test
    public void testSubmitNullUser() {
        when(userRepo.findByUsername("test")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUser() {
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(new ArrayList<>());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
        assertEquals(0, orders.size());
    }

    @Test
    public void testGetOrdersForUserNullUser() {
        when(userRepo.findByUsername("test")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}

