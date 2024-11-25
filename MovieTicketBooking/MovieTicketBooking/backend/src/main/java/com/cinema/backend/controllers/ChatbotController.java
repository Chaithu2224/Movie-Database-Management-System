package com.cinema.backend.controllers;

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