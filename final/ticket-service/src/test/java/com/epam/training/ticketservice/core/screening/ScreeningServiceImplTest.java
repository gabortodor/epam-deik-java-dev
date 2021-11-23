package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class ScreeningServiceImplTest {
    private final ScreeningRepository screeningRepository  = mock(ScreeningRepository.class);

    private final RoomService roomService = mock(RoomServiceImpl.class);
    private final MovieService movieService = mock(MovieServiceImpl.class);

    private ScreeningService underTest = new ScreeningServiceImpl(screeningRepository,movieService,roomService);

    @Test
    public void testCreateScreeningShouldThrowNullPointerExceptionWhenRoomIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createScreening(null));
    }

    @Test
    public void testCreateScreeningShouldThrowNullPointerExceptionWhenMovieTitleIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle(null)
                .roomName("test")
                .startingTime("2021-10-10 16:30")
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));
    }

    @Test
    public void testCreateScreeningShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("test")
                .roomName(null)
                .startingTime("2021-10-10 16:30")
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));
    }

    @Test
    public void testCreateScreeningShouldThrowNullPointerExceptionWhenStartingTimeIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("test")
                .roomName("test")
                .startingTime(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));
    }
}
