package com.cinema.backend.controllers;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stripe.net.HttpHeaders;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessageToBot(@RequestBody String userMessage) {
        String botpressApiUrl = "http://localhost:3000/api/v1/bots/movie-bot/converse";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("text", userMessage);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
    }
}
