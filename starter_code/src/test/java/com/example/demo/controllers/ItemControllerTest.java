package com.example.demo.controllers;

import org.junit.Test;
import java.util.List;
import org.junit.Before;
import java.util.Optional;
import java.util.ArrayList;
import java.math.BigDecimal;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem()));
        List<Item> itemRepository = new ArrayList<>();
        itemRepository.add(createItem());
        when(this.itemRepository.findByName("Item Test")).thenReturn(itemRepository);
    }

    @Test
    public void getAllItemTest(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> itemRepository = response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(itemRepository);
    }

    @Test
    public void getItemByIdTest(){
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Item item = response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(item);
    }

    @Test
    public void getItemByNameTest(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item Test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getItemByNameInvalidTest(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("invalid Test");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Item Test");
        item.setPrice(BigDecimal.valueOf(55.0));
        item.setDescription("Description item test ...");
        return item;
    }

}