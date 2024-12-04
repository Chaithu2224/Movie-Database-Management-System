package com.cinema.backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinema.backend.services.PaymentService;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3001/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // POST mapping for payment end point
    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPaymentIntent(@RequestBody Map<String, Object> request) throws Exception {
        int amount = (int) request.get("amount");
        return paymentService.createPaymentIntent(amount);
    }
}
