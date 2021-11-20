package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void createMovie(String title, String genre, Integer runtime) {
        Movie movie = new Movie(title, genre, runtime);
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(String title, String genre, Integer runtime) {
        Movie movie = new Movie(title, genre, runtime);
        movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(String title) {
        Optional<Movie> movie = movieRepository.findById(title);
        if (movie.isPresent())
            movieRepository.delete(movie.get());
    }

    @Override
    public List<Movie> listMovies() {
        return movieRepository.findAll();
    }
}
