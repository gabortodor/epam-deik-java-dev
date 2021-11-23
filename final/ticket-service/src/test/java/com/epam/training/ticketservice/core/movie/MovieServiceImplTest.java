package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

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
}
