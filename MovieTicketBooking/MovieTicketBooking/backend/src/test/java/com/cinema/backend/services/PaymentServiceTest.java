package com.cinema.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentIntent mockPaymentIntent;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePaymentIntent_withValidAmount() throws Exception {
        int amount = 50; // Amount in dollars

        // Mock PaymentIntent response from Stripe
        when(mockPaymentIntent.getClientSecret()).thenReturn("test_secret");
        when(PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(mockPaymentIntent);

        Map<String, Object> result = paymentService.createPaymentIntent(amount);

        // Verify the response
        assertEquals("test_secret", result.get("clientSecret"));
    }

    @Test
    void testCreatePaymentIntent_handlesException() {
        int amount = 50;

        // Simulate an exception when calling PaymentIntent.create
        when(PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                .thenThrow(new RuntimeException("Stripe API error"));

        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.createPaymentIntent(amount);
        });

        assertEquals("Stripe API error", exception.getMessage());
    }
}
