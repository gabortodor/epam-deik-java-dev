package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;

import java.util.List;

public interface MovieService {

    void createMovie(String title, String genre, Integer runtime);

    void updateMovie(String title, String genre, Integer runtime);

    void deleteMovie(String title);

    List<Movie> listMovies();
}
