package com.example.demo.controllerTests;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.controllers.ItemController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    private Item item0;
    private Item item1;

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {

        item0 = new Item();
        item0.setId(0L);
        item0.setName("Round Widget");
        item0.setDescription("A widget that is round.");

        item1 = new Item();
        item1.setId(1L);
        item1.setName("Square Widget");
        item1.setDescription("A widget that is square.");
    }


    @Test
    public void testGetItems() {

        List<Item> items = new ArrayList<>();
        items.add(item0);
        items.add(item1);
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(2, retrievedItems.size());
        assertEquals(item0, retrievedItems.get(0));
        assertEquals(item1, retrievedItems.get(1));
    }

    @Test
    public void testGetItemById() {

        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item0));

        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item retrievedItem = response.getBody();
        assertNotNull(retrievedItem);
        assertEquals(item0.getName(), retrievedItem.getName());
        assertEquals(item0.getId(), retrievedItem.getId());
        assertEquals(item0.getDescription(), retrievedItem.getDescription());
    }

    @Test
    public void testGetItemsByName() {

        List<Item> items = new ArrayList<>();
        items.add(item0);
        when(itemRepository.findByName("Round Widget")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(1, retrievedItems.size());
        assertEquals(item0, retrievedItems.get(0));
    }
}

