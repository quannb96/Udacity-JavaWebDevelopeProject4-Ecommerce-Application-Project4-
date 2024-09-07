package com.example.demo.controllers;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import static org.junit.Assert.*;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUserTest() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("QuanNB2");
        userRequest.setPassword("password1!");
        userRequest.setConfirmPassword("password1!");
        when(bCryptPasswordEncoder.encode("password1!")).thenReturn("HashedPasswordMock");

        ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User userRes = response.getBody();
        assertNotNull(userRes);
        assertEquals(0, userRes.getId());
        assertEquals("QuanNB2", userRes.getUsername());
        assertEquals("HashedPasswordMock", userRes.getPassword());
    }

    @Test
    public void findByIdTest() {
        User user = createUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    public void findByUsernameTest() {
        User user = createUser();
        when(userRepository.findByUsername("QuanNB2")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("QuanNB2");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("QuanNB2", response.getBody().getUsername());
    }

    @Test
    public void findByIdNotFoundTest() {
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByUsernameNotFoundTest() {
        ResponseEntity<User> response = userController.findByUserName("QuanNB2");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    public static User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("QuanNB2");
        user.setPassword("password1!");
        return user;
    }

}