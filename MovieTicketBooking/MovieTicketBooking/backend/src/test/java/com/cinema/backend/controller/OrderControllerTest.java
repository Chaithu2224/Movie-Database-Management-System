package com.cinema.backend.controller;

import com.cinema.backend.models.Order;
import com.cinema.backend.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(OrderControllerTest.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Test for creating a new order
    @Test
    void testCreateNewOrder() throws Exception {
        Order newOrder = new Order(1L, 100L, LocalDateTime.now(), 25.00);
        Order savedOrder = new Order(1L, 100L, LocalDateTime.now(), 25.00);

        // Mocking the repository save behavior
        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Perform the POST request to create a new order
        mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerId").value(100L))
                .andExpect(jsonPath("$.amount").value(25.00));
    }

    @Test
    void testGetLastOrderByUserId_NotFound() throws Exception {
        Long userId = 100L;

        Mockito.when(orderRepository.findFirstByCustomerIdOrderByCreatedAtDesc(eq(userId)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/order/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
