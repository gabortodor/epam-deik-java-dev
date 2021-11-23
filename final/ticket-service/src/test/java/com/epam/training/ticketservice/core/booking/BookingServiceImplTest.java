package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.impl.BookingServiceImpl;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class BookingServiceImplTest {
    private final BookingRepository bookingRepository  = mock(BookingRepository.class);

    private final RoomService roomService = mock(RoomServiceImpl.class);
    private final MovieService movieService = mock(MovieServiceImpl.class);
    private final ScreeningService screeningService = mock(ScreeningServiceImpl.class);

    private BookingService underTest = new BookingServiceImpl(bookingRepository, screeningService, roomService, movieService);


    @Test
    public void testCreateBookingShouldThrowNullPointerExceptionWhenBookingIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createBooking(null));
    }

    @Test
    public void testCreateBookingShouldThrowNullPointerExceptionWhenMovieTitleIsNull() {
        // Given
        BookingDto bookingDto = BookingDto.builder()
                .movieTitle(null)
                .roomName("test")
                .startingTime("2021-10-10 16:30")
                .seats("5,5 5,6")
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createBooking(bookingDto));
    }

    @Test
    public void testCreateBookingShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        // Given
        BookingDto bookingDto = BookingDto.builder()
                .movieTitle("test")
                .roomName(null)
                .startingTime("2021-10-10 16:30")
                .seats("5,5 5,6")
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createBooking(bookingDto));
    }

    @Test
    public void testCreateBookingShouldThrowNullPointerExceptionWhenStartingTimeIsNull() {
        // Given
        BookingDto bookingDto = BookingDto.builder()
                .movieTitle("test")
                .roomName("test")
                .startingTime(null)
                .seats("5,5 5,6")
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createBooking(bookingDto));
    }

    @Test
    public void testCreateBookingShouldThrowNullPointerExceptionWhenSeatsIsNull() {
        // Given
        BookingDto bookingDto = BookingDto.builder()
                .movieTitle("test")
                .roomName("test")
                .startingTime("2021-10-10 16:30")
                .seats(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createBooking(bookingDto));
    }
}
