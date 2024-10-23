package com.cinema.backend.controller;

import com.cinema.backend.dto.LoginResponseDTO;
import com.cinema.backend.models.User;
import com.cinema.backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserControllerTest.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test for user registration
    @Test
    void testRegisterUser() throws Exception {
        User newUser = new User("John Doe", "john.doe@example.com", "password123");
        User savedUser = new User(1L, "John Doe", "john.doe@example.com", "password123");

        // Mocking the service behavior
        Mockito.when(userService.registerUser(any(User.class))).thenReturn(savedUser);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    // Test for successful user login
    @Test
    void testLoginUserSuccess() throws Exception {
        User loginRequest = new User("john.doe@example.com", "password123");
        User existingUser = new User(1L, "John Doe", "john.doe@example.com", "password123");

        // Mocking the service behavior
        Mockito.when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(existingUser);
        Mockito.when(userService.isPasswordMatch(eq("password123"), eq("password123"))).thenReturn(true);

        // Expected response after successful login
        LoginResponseDTO response = new LoginResponseDTO("Login successful", "John Doe", 1L);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.userName").value("John Doe"))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    // Test for failed user login due to invalid credentials
    @Test
    void testLoginUserInvalidCredentials() throws Exception {
        User loginRequest = new User("john.doe@example.com", "wrongpassword");
        User existingUser = new User(1L, "John Doe", "john.doe@example.com", "password123");

        // Mocking the service behavior
        Mockito.when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(existingUser);
        Mockito.when(userService.isPasswordMatch(eq("wrongpassword"), eq("password123"))).thenReturn(false);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"))
                .andExpect(jsonPath("$.userName").isEmpty())
                .andExpect(jsonPath("$.userId").isEmpty());
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        User loginRequest = new User("john.doe@example.com", "password123");

        Mockito.when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(null);

        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"))
                .andExpect(jsonPath("$.userName").isEmpty())
                .andExpect(jsonPath("$.userId").isEmpty());
    }
}
