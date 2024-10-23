package com.cinema.backend.controller;

import com.cinema.backend.models.CinemaHall;
import com.cinema.backend.repositories.CinemaHallRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import java.util.Optional;

@WebMvcTest(CinemaHallControllerTest.class)
public class CinemaHallControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CinemaHallRepository cinemaHallRepository;

    @Test
    void testGetUpdatedSeats() throws Exception {
        // Mock the repository response
        when(cinemaHallRepository.findByMovieIdAndMovieSession(456L, "Morning"))
                .thenReturn(Optional.of(new CinemaHall()));

        // Get request
        mockMvc.perform(get("/api/v1/movie/456/Morning"))
                .andExpect(status().isOk());
    }
}
