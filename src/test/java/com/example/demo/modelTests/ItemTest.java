package com.example.demo.modelTests;

import com.example.demo.model.persistence.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {

    @Test
    public void testItemProperties() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(new BigDecimal("19.99"));
        item.setDescription("Test Description");

        assertEquals(1L, item.getId());
        assertEquals("Test Item", item.getName());
        assertEquals(new BigDecimal("19.99"), item.getPrice());
        assertEquals("Test Description", item.getDescription());
    }
}


