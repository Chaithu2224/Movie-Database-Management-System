package com.cinema.backend.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PaymentService {

    // Set the API key for Stripe from application.properties
    public PaymentService(@Value("${stripe.api.key}") String apiKey) {
        Stripe.apiKey = apiKey;
    }

    // Method to create a PaymentIntent
    public Map<String, Object> createPaymentIntent(int amount) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) amount * 100) // Convert dollars to cents
                .setCurrency("usd")
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        return response;
    }
}
