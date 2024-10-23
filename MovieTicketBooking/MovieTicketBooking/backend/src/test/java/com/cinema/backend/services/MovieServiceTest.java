package com.cinema.backend.service;

import com.cinema.backend.models.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceTest {

    // Sample data to simulate a database
    private List<Movie> movies = new ArrayList<>();

    public MovieServiceTest() {
        movies.add(new Movie(1L, "Inception", "Action", "Christopher Nolan", 2010));
        movies.add(new Movie(2L, "Interstellar", "Sci-Fi", "Christopher Nolan", 2014));
        movies.add(new Movie(3L, "The Dark Knight", "Action", "Christopher Nolan", 2008));
    }

    // Method to find a movie by its ID
    public Movie findMovieById(Long id) {
        Optional<Movie> movie = movies.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
        return movie.orElse(null); // Return the movie if found, otherwise return null
    }

    // Optionally, you can add other methods like getting all movies or searching by
    // genre
    public List<Movie> getAllMovies() {
        return movies;
    }
}
