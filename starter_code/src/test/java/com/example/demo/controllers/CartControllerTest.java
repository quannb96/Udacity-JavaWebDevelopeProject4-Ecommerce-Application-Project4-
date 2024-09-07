package com.example.demo.controllers;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("QuanNB2")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addToCartTest(){
        ModifyCartRequest modifyCartRequest = createModifyCartRequest(1, "QuanNB2", 1);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Cart resCart = response.getBody();
        assertNotNull(resCart);
        assertEquals("QuanNB2", resCart.getUser().getUsername());
        verify(cartRepository, times(1)).save(resCart);
        List<Item> itemRepository = resCart.getItems();
        assertNotNull(itemRepository);
    }

    @Test
    public void removeFromCartTest(){
        ModifyCartRequest modifyCartRequest = createModifyCartRequest(1, "QuanNB2", 1);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Cart resCart = response.getBody();
        assertNotNull(resCart);
        assertEquals("QuanNB2", resCart.getUser().getUsername());
        verify(cartRepository, times(1)).save(resCart);
        List<Item> itemRepository = resCart.getItems();
        assertNotNull(itemRepository);
        assertEquals(0, itemRepository.size());
    }

    @Test
    public void invalidTest(){
        ModifyCartRequest modifyCartRequest = createModifyCartRequest(1, "invalidTest", 1);

        ResponseEntity<Cart> removeRes = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(removeRes);
        assertEquals(HttpStatus.NOT_FOUND, removeRes.getStatusCode());

        ResponseEntity<Cart> addResponse = cartController.addTocart(modifyCartRequest);
        assertNotNull(addResponse);
        assertEquals(404, addResponse.getStatusCodeValue());
    }
    
    public static ModifyCartRequest createModifyCartRequest(long itemId, String username, int quantity){
        ModifyCartRequest modifyCartReq = new ModifyCartRequest();
        modifyCartReq.setItemId(itemId);
        modifyCartReq.setUsername(username);
        modifyCartReq.setQuantity(quantity);
        return modifyCartReq;
    }

    public static User createUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("QuanNB2");
        user.setPassword("Password1!");

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<Item>());
        cart.setTotal(BigDecimal.valueOf(0.0));

        return user;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Item Test");
        item.setPrice(BigDecimal.valueOf(10.0));
        item.setDescription("Description item test ...");
        return item;
    }

}