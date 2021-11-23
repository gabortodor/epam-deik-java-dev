package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceImplTest {

    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private MovieService underTest = new MovieServiceImpl(movieRepository);

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(null));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieTitleIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title(null)
                .genre("comedy")
                .runtime(120)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movieDto));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieGenreIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("test")
                .genre(null)
                .runtime(120)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movieDto));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieRuntimeIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("test")
                .genre("comedy")
                .runtime(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movieDto));
    }

    @Test
    public void testCreateMovieShouldCallMovieRepositoryWhenTheInputIsValid() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("testTitle")
                .genre("comedy")
                .runtime(120)
                .build();
        // When
        underTest.createMovie(movieDto);

        // Then
        verify(movieRepository).save(new Movie("testTitle", "comedy", 120));
    }

    @Test
    public void testUpdateMovieShouldThrowNullPointerExceptionWhenMovieIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.updateMovie(null));
    }

    @Test
    public void testUpdateMovieShouldThrowNullPointerExceptionWhenMovieTitleIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title(null)
                .genre("comedy")
                .runtime(120)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.updateMovie(movieDto));
    }

    @Test
    public void testUpdateMovieShouldThrowNullPointerExceptionWhenMovieGenreIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("test")
                .genre(null)
                .runtime(120)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.updateMovie(movieDto));
    }

    @Test
    public void testUpdateMovieShouldThrowNullPointerExceptionWhenMovieRuntimeIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("test")
                .genre("comedy")
                .runtime(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.updateMovie(movieDto));
    }

    @Test
    public void testUpdateMovieShouldCallMovieRepositoryWhenTheInputIsValid() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("testTitle")
                .genre("comedy")
                .runtime(120)
                .build();
        // When
        underTest.updateMovie(movieDto);

        // Then
        verify(movieRepository).save(new Movie("testTitle", "comedy", 120));
    }

    @Test
    public void testDeleteMovieShouldCallMovieRepository() {
        //Given-When
        underTest.deleteMovie("testTitle");

        // Then
        verify(movieRepository).deleteById("testTitle");
    }


    @Test
    public void testGetMovieListShouldCallMovieRepositoryAndReturnADtoList() {
        // Given
        Movie movie1= new Movie("testTitle1","comedy",120);
        Movie movie2= new Movie("testTitle2","drama",90);
        MovieDto movieDto1 = MovieDto.builder().title("testTitle1").genre("comedy").runtime(120).changeInPrice(0).build();
        MovieDto movieDto2 = MovieDto.builder().title("testTitle2").genre("drama").runtime(90).changeInPrice(0).build();
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        List<MovieDto> expected = List.of(movieDto1, movieDto2);

        // When
        List<MovieDto> actual = underTest.getMovieList();

        // Then
        assertEquals(expected, actual);
        verify(movieRepository).findAll();
    }

    @Test
    public void testGetMovieByTitleShouldReturnOptionalEmptyWhenInputMovieTitleDoesNotExist() {
        // Given
        when(movieRepository.findById("testTitle")).thenReturn(Optional.empty());
        Optional<MovieDto> expected = Optional.empty();

        // When
        Optional<MovieDto> actual = underTest.getMovieByTitle("testTitle");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(movieRepository).findById("testTitle");
    }

    @Test
    public void testGetMovieByTitleShouldReturnMovieWhenInputMovieTitleIsValid() {
        // Given
        Movie movie = new Movie("testTitle", "comedy", 120);
        MovieDto movieDto = MovieDto.builder().title("testTitle").genre("comedy").runtime(120).changeInPrice(0).build();
        when(movieRepository.findById("testTitle")).thenReturn(Optional.of(movie));
        Optional<MovieDto> expected = Optional.of(movieDto);

        // When
        Optional<MovieDto> actual = underTest.getMovieByTitle("testTitle");

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
        verify(movieRepository).findById("testTitle");
    }

    @Test
    public void testUpdateChangeInPriceShouldReturnMessageIfGivenMovieIsNotValid() {
        // Given
        when(movieRepository.findById("testTitle")).thenReturn(Optional.empty());
        String expected = "No such movie exists";

        // When
        String actual = underTest.updateChangeInPrice("testTitle",100);

        // Then
        assertEquals(expected, actual);
        verify(movieRepository).findById("testTitle");
    }

    @Test
    public void testUpdateChangeInPriceShouldSetChangeInPriceAndCallMovieRepositoryAndReturnMessageIfGivenMovieIsValid() {
        // Given
        when(movieRepository.findById("testTitle")).thenReturn(Optional.of(new Movie("testTitle","comedy",120,0)));
        String expected = "Price component successfully applied";

        // When
        String actual = underTest.updateChangeInPrice("testTitle",100);

        // Then
        assertEquals(expected, actual);
        verify(movieRepository).save(new Movie("testTitle","comedy",120,100));
    }
}
