package com.cinema.backend.services;

import com.cinema.backend.models.CinemaHall;
import com.cinema.backend.repositories.CinemaHallRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CinemaHallServiceTest {

    @Mock
    private CinemaHallRepository cinemaHallRepository;

    @InjectMocks
    private CinemaHallServiceTest cinemaHallService;

    public CinemaHallServiceTest() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testFindCinemaHall() {
        CinemaHall hall = new CinemaHall();
        when(cinemaHallRepository.findByMovieIdAndMovieSession(456L, "Morning"))
                .thenReturn(Optional.of(hall));

        Optional<CinemaHall> result = cinemaHallService.findCinemaHall(456L, "Morning");
        assertTrue(result.isPresent());
    }
}