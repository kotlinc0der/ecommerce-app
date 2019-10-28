package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController(userRepository, cartRepository, passwordEncoder);
    }

    @Test
    public void whenFindUserById_thenUserIsReturned() {
        long id = 1L;
        User mockUser = new User();
        mockUser.setId(id);
        mockUser.setUsername(TEST_USERNAME);
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.findById(id);
        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        Assert.assertEquals(user.getId(), id);
    }

    @Test
    public void whenFindUserByUsername_thenUserIsReturned() {
        User mockUser = new User();
        mockUser.setUsername(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.findByUserName(TEST_USERNAME);
        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        Assert.assertEquals(user.getUsername(), TEST_USERNAME);
    }

    @Test
    public void whenFindUserByNonExistentUsername_thenNotFoundErrorIsReturned() {
        User mockUser = new User();
        mockUser.setUsername(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.findByUserName("");
        Assert.assertEquals(response.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenCreationRequestHasValidCredentials_thenUserIsCreated() {
        when(passwordEncoder.encode(anyString())).thenReturn(TEST_PASSWORD);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(TEST_USERNAME);
        createUserRequest.setPassword(TEST_PASSWORD);
        createUserRequest.setConfirmPassword(TEST_PASSWORD);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        User createdUser = response.getBody();

        Assert.assertNotNull(createdUser);
        Assert.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        Assert.assertEquals(createdUser.getUsername(), TEST_USERNAME);
        Assert.assertEquals(createdUser.getPassword(), TEST_PASSWORD);
    }

    @Test
    public void whenCreationRequestHasInvalidPassword_thenUserIsNotCreated() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(TEST_USERNAME);
        createUserRequest.setPassword(TEST_PASSWORD);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        Assert.assertEquals(response.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenCreationRequestIsWithoutMatchingPassword_thenUserIsNotCreated() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(TEST_USERNAME);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        Assert.assertEquals(response.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }
}
