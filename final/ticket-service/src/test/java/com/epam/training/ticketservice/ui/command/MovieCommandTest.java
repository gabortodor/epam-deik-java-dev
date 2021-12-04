package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MovieCommandTest {

    private final MovieService movieService = mock(MovieServiceImpl.class);
    private final UserService userService = mock(UserServiceImpl.class);

    private final MovieCommand underTest = new MovieCommand(movieService, userService);

    private final MovieDto MOVIE_DTO = MovieDto.builder().title("testMovieTitle").genre("comedy").runtime(120).build();

    @Test
    public void testListMoviesShouldReturnCorrectMessageWhenThereAreNoMovies(){
        // Given
        when(movieService.getMovieList()).thenReturn(List.of());
        String expected = "There are no movies at the moment";

        // When
        String actual = underTest.listMovies();

        // Then
        assertEquals(expected, actual);
        verify(movieService).getMovieList();
    }

    @Test
    public void testListMoviesShouldReturnMovieListWhenThereAreMovies(){
        // Given
        when(movieService.getMovieList()).thenReturn(List.of(MovieDto.builder()
                .title("testMovieTitle1")
                .genre("comedy")
                .runtime(120).build(), MovieDto.builder()
                .title("testMovieTitle2")
                .genre("drama")
                .runtime(90).build()));
        String expected =  "testMovieTitle1 (comedy, 120 minutes)\ntestMovieTitle2 (drama, 90 minutes)";

        // When
        String actual = underTest.listMovies();

        // Then
        assertEquals(expected, actual);
        verify(movieService).getMovieList();
    }

    @Test
    public void testCreateMovieShouldReturnCorrectMessageWhenMovieIsValid() {
        // Given
        doNothing().when(movieService).createMovie(MOVIE_DTO);
        String expected = "testMovieTitle was successfully created!";

        // When
        String actual = underTest.createMovie("testMovieTitle","comedy",120);

        //Then
        assertEquals(expected, actual);
        verify(movieService).createMovie(MOVIE_DTO);
    }

    @Test
    public void testUpdateMovieShouldReturnCorrectMessageWhenMovieIsValid() {
        // Given
        doNothing().when(movieService).updateMovie(MOVIE_DTO);
        String expected = "testMovieTitle was successfully updated!";

        // When
        String actual = underTest.updateMovie("testMovieTitle","comedy",120);

        //Then
        assertEquals(expected, actual);
        verify(movieService).updateMovie(MOVIE_DTO);
    }

    @Test
    public void testDeleteMovieShouldReturnCorrectMessageWhenMovieIsValid() {
        // Given
        doNothing().when(movieService).deleteMovie("testMovieTitle");
        String expected = "testMovieTitle was successfully deleted!";

        // When
        String actual = underTest.deleteMovie("testMovieTitle");

        //Then
        assertEquals(expected, actual);
        verify(movieService).deleteMovie("testMovieTitle");
    }




}
