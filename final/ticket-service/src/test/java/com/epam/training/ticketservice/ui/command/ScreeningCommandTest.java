package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ScreeningCommandTest {

    private final ScreeningService screeningService = mock(ScreeningServiceImpl.class);
    private final UserService userService = mock(UserServiceImpl.class);
    private final MovieService movieService = mock(MovieServiceImpl.class);

    private final ScreeningCommand underTest = new ScreeningCommand(screeningService,userService,movieService);

    private final ScreeningDto SCREENING_DTO_1 = ScreeningDto.builder()
            .movieTitle("testMovieTitle")
            .roomName("testRoomName")
            .startingTime("2021-10-10 16:30")
            .build();

    private final ScreeningDto SCREENING_DTO_2 = ScreeningDto.builder()
            .movieTitle("testMovieTitle")
            .roomName("testRoomName")
            .startingTime("2021-10-11 12:30")
            .build();

    @Test
    public void testListScreeningsShouldReturnCorrectMessageWhenThereAreNoScreenings(){
        // Given
        when(screeningService.getScreeningList()).thenReturn(List.of());
        String expected = "There are no screenings";

        // When
        String actual = underTest.listScreenings();

        // Then
        assertEquals(expected, actual);
        verify(screeningService).getScreeningList();
    }

    @Test
    public void testListScreeningsShouldReturnScreeningListWhenThereAreScreenings(){
        // Given
        when(screeningService.getScreeningList()).thenReturn(List.of(SCREENING_DTO_1, SCREENING_DTO_2));
        when(movieService.getMovieByTitle("testMovieTitle")).thenReturn(Optional.of(MovieDto.builder().title("testMovieTitle").genre("comedy").runtime(120).build()));
        String expected = "testMovieTitle (comedy, 120 minutes), screened in room testRoomName, at 2021-10-10 16:30\n"+
                            "testMovieTitle (comedy, 120 minutes), screened in room testRoomName, at 2021-10-11 12:30";

        // When
        String actual = underTest.listScreenings();

        // Then
        assertEquals(expected, actual);
        verify(screeningService).getScreeningList();
    }




}
