package com.example.demo.controllers;

import java.util.List;
import org.junit.Test;
import org.junit.Before;
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
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void submitTest(){
        Item item = createItem();
        List<Item> items = new ArrayList<>();
        items.add(item);
        
        User user = createUser();
        Cart cart = user.getCart();
        cart.setUser(user);
        cart.setItems(items);
        user.setCart(cart);

        when(userRepository.findByUsername("QuanNB2")).thenReturn(user);

        ResponseEntity<UserOrder> response =  orderController.submit("QuanNB2");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserOrder orderRes = response.getBody();
        assertNotNull(orderRes);
        assertNotNull(orderRes.getUser());
        assertEquals(createUser().getId(), orderRes.getUser().getId());
    }

    @Test
    public void getOrdersForUserTest(){
        Item item = createItem();
        List<Item> items = new ArrayList<>();
        items.add(item);
        
        User user = createUser();
        Cart cart = user.getCart();
        cart.setUser(user);
        cart.setItems(items);
        user.setCart(cart);

        when(userRepository.findByUsername("QuanNB2")).thenReturn(user);
        orderController.submit("QuanNB2");

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("QuanNB2");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<UserOrder> userOrdersRes = response.getBody();
        assertNotNull(userOrdersRes);
        assertEquals(0, userOrdersRes.size());
    }

    @Test
    public void submitInvalidUserTest() {
        when(userRepository.findByUsername("QuanNB2")).thenReturn(null);
        ResponseEntity<UserOrder> response =  orderController.submit("QuanNB2");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrdersForInvalidUserTest(){
        when(userRepository.findByUsername("QuanNB2")).thenReturn(null);
        orderController.submit("QuanNB2");

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("QuanNB2");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Item Test");
        item.setDescription("Description item test ...");
        item.setPrice(BigDecimal.valueOf(55.0));
        return item;
    }

    public static User createUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("QuanNB2");
        user.setPassword("password1!");

        Cart emptyCart = new Cart();
        emptyCart.setId(1L);
        emptyCart.setUser(null);
        emptyCart.setItems(new ArrayList<Item>());
        emptyCart.setTotal(BigDecimal.valueOf(0.0));
        user.setCart(emptyCart);

        return user;
    }

}