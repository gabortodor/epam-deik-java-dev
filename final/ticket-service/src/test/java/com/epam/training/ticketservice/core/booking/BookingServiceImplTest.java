package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.impl.BookingServiceImpl;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.pricecomponent.impl.PriceComponentServiceImpl;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {
    private final BookingDto BOOKING_DTO = BookingDto.builder()
            .movieTitle("testMovieTitle")
            .roomName("testRoomName")
            .startingTime("2021-10-10 16:30")
            .seats("5,5 5,6")
            .build();


    private final ScreeningDto SCREENING_DTO = ScreeningDto.builder()
            .movieTitle("testMovieTitle")
            .roomName("testRoomName")
            .startingTime("2021-10-10 16:30")
            .changeInPrice(0)
            .build();

    private final MovieDto MOVIE_DTO = MovieDto.builder()
            .title("testMovieTitle")
            .genre("comedy")
            .runtime(120)
            .changeInPrice(0)
            .build();

    private final Screening.ScreeningKey SCREENING_KEY_ENTITY =
            new Screening.ScreeningKey("testMovieTitle","testRoomName","2021-10-10 16:30");

    private final Booking BOOKING_ENTITY =
            new Booking("testMovieTitle", "testRoomName", "2021-10-10 16:30", "5,5 5,6","user",0);


    private final BookingRepository bookingRepository  = mock(BookingRepository.class);

    private final RoomService roomService = mock(RoomServiceImpl.class);
    private final ScreeningService screeningService = mock(ScreeningServiceImpl.class);
    private final PriceComponentService priceComponentService = mock(PriceComponentServiceImpl.class);

    private BookingService underTest = new BookingServiceImpl(bookingRepository, roomService, screeningService, priceComponentService);


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

    @Test
    public void testCreateBookingShouldCallScreeningServiceAndReturnStringMessageWhenBookingsScreeningIsInvalid(){
        // Given
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.empty());
        String expected =  "Cannot find specified screening";

        // When
        String actual = underTest.createBooking(BOOKING_DTO);

        // Then
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(SCREENING_KEY_ENTITY);
    }

    @Test
    public void testCreateBookingShouldCallScreeningServiceAndReturnStringMessageWhenBookingsSeatsAreInvalid(){
        // Given
        RoomDto roomDto = RoomDto.builder().name("testRoomName").rowCount(4)
                .columnCount(4).changeInPrice(0).build();
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(SCREENING_DTO));
        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.of(roomDto));
        String expected =  "Seat (5,5) does not exists in this room";

        // When
        String actual = underTest.createBooking(BOOKING_DTO);

        // Then
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(SCREENING_KEY_ENTITY);
        verify(roomService).getRoomByName("testRoomName");
    }

    @Test
    public void testCreateBookingShouldCallScreeningServiceAndReturnStringMessageWhenBookingsSeatsAreBooked(){
        // Given
        RoomDto roomDto = RoomDto.builder().name("testRoomName").rowCount(10)
                .columnCount(10).changeInPrice(0).build();
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(SCREENING_DTO));
        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.of(roomDto));
        when(bookingRepository.findAllByMovieTitleAndRoomNameAndStartingTime("testMovieTitle","testRoomName","2021-10-10 16:30"))
                .thenReturn(List.of(BOOKING_ENTITY));
        String expected =  "Seat (5,5) is already taken";

        // When
        String actual = underTest.createBooking(BOOKING_DTO);

        // Then
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(SCREENING_KEY_ENTITY);
        verify(roomService).getRoomByName("testRoomName");
        verify(bookingRepository).findAllByMovieTitleAndRoomNameAndStartingTime("testMovieTitle","testRoomName","2021-10-10 16:30");
    }


}
