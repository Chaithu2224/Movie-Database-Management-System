package com.cinema.backend.controller;

import com.cinema.backend.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("clientSecret", "test_secret");

        when(paymentService.createPaymentIntent(anyInt())).thenReturn(mockResponse);
    }

    @Test
    void testCreatePaymentIntent_successful() throws Exception {
        mockMvc.perform(post("/api/payment/create-payment-intent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientSecret").value("test_secret"));
    }

    @Test
    void testCreatePaymentIntent_invalidAmount() throws Exception {
        mockMvc.perform(post("/api/payment/create-payment-intent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": \"invalid\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePaymentIntent_missingAmount() throws Exception {
        mockMvc.perform(post("/api/payment/create-payment-intent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePaymentIntent_serviceException() throws Exception {
        when(paymentService.createPaymentIntent(anyInt())).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/api/payment/create-payment-intent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 50}"))
                .andExpect(status().isInternalServerError());
    }
}
