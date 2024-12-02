
package com.cinema.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessageToBot(@RequestBody String userMessage) {
        String botpressApiUrl = "http://localhost:3001/api/v1/bots/movie-bot/converse";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("text", userMessage);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(botpressApiUrl, HttpMethod.POST, entity,
                    String.class);
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error communicating with Botpress.");
        }
    }
}
