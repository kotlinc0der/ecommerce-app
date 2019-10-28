package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private static final String USERNAME = "username";
    private static final long ITEM_ID = 1L;
    private static final String PRICE = "21.45";

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController(userRepository, orderRepository);
        when(userRepository.findByUsername(USERNAME)).thenReturn(getUser());
        when(orderRepository.findByUser(any())).thenReturn(getUserOrders());
    }

    @Test
    public void whenSubmitUserOrder_thenOrderIsReturned() {
        ResponseEntity<UserOrder> response = orderController.submit(USERNAME);
        UserOrder userOrder = response.getBody();

        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertNotNull(userOrder);
        assertEquals(userOrder.getUser().getUsername(), USERNAME);
        assertEquals(userOrder.getItems().size(), 1);
        assertEquals(userOrder.getTotal(), new BigDecimal(PRICE));
    }

    @Test
    public void whenSubmitUserOrderWithInvalidUsername_thenNotFoundErrorIsReturned() {
        ResponseEntity<UserOrder> response = orderController.submit("");
        assertEquals(response.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenGetOrdersForUser_thenUserOrdersReturned() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(USERNAME);
        List<UserOrder> userOrders = response.getBody();

        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertNotNull(userOrders);
        assertEquals(userOrders.size(), 1);

        UserOrder userOrder = userOrders.get(0);
        assertEquals(userOrder.getUser().getUsername(), USERNAME);
        assertEquals(userOrder.getTotal(), new BigDecimal(PRICE));
        assertEquals(userOrder.getItems().size(), 1);
    }

    @Test
    public void whenGetOrdersForInvalidUser_thenNotFoundErrorIsReturned() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("");
        assertEquals(response.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }

    private static User getUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setCart(getCart(user));
        return user;
    }

    private static Cart getCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(getItem().orElse(null));
        return cart;
    }

    private static Optional<Item> getItem() {
        Item item = new Item();
        item.setId(ITEM_ID);
        item.setPrice(new BigDecimal(PRICE));
        return Optional.of(item);
    }

    private static List<UserOrder> getUserOrders() {
        UserOrder userOrder = UserOrder.createFromCart(getUser().getCart());
        return Lists.list(userOrder);
    }
}
