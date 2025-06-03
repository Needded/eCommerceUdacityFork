package com.example.demo.controllerTests;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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
public class CartControllerTest {

    public static final String USERNAME = "test";

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CartRepository cartRepo;

    @Mock
    private ItemRepository itemRepository;

    private User user;
    private Item item;
    private Cart cart;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername(USERNAME);

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
    public void addToCart() {
        when(userRepo.findByUsername(USERNAME)).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername(USERNAME);

        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals(0L, retrievedCart.getId());

        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(item, items.get(0));
        assertEquals(item, items.get(1));

        assertEquals(new BigDecimal("5.98"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }

    @Test
    public void testAddToCartNullUser() {
        when(userRepo.findByUsername(USERNAME)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername(USERNAME);

        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart() {
        when(userRepo.findByUsername(USERNAME)).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername(USERNAME);

        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals(0L, retrievedCart.getId());

        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());

        assertEquals(new BigDecimal("0.00"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }
}
