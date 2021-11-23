package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    void createMovie(MovieDto movieDto);

    void updateMovie(MovieDto movieDto);

    void deleteMovie(String title);

    List<MovieDto> getMovieList();

    Optional<MovieDto> getMovieByTitle(String title);

    String updateChangeInPrice(String title, int priceChange);
}
