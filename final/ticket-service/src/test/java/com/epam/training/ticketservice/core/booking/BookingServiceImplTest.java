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

import static org.junit.jupiter.api.Assertions.*;
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


    private final Screening.ScreeningKey SCREENING_KEY_ENTITY =
            new Screening.ScreeningKey("testMovieTitle","testRoomName","2021-10-10 16:30");

    private final Booking BOOKING_ENTITY =
            new Booking("testMovieTitle", "testRoomName", "2021-10-10 16:30", "5,5 5,6","user",0);

    private final BookingRepository bookingRepository  = mock(BookingRepository.class);

    private final RoomService roomService = mock(RoomServiceImpl.class);
    private final ScreeningService screeningService = mock(ScreeningServiceImpl.class);
    private final PriceComponentService priceComponentService = mock(PriceComponentServiceImpl.class);

    private final BookingService underTest = new BookingServiceImpl(bookingRepository, roomService, screeningService, priceComponentService);


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

    @Test
    public void testCreateBookingShouldCallBookingRepositoryAndReturnStringMessageWhenBookingIsValid(){
        // Given
        RoomDto roomDto = RoomDto.builder().name("testRoomName").rowCount(10)
                .columnCount(10).changeInPrice(0).build();
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(SCREENING_DTO));
        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.of(roomDto));
        when(bookingRepository.findAllByMovieTitleAndRoomNameAndStartingTime("testMovieTitle","testRoomName","2021-10-10 16:30"))
                .thenReturn(List.of());
        String expected =  "Seats booked: (5,5), (5,6); the price for this booking is 3000 HUF";

        // When
        String actual = underTest.createBooking(BOOKING_DTO);

        // Then
        assertEquals(expected, actual);
        verify(screeningService, times(2)).getScreeningByKey(SCREENING_KEY_ENTITY);
        verify(roomService).getRoomByName("testRoomName");
        verify(bookingRepository).findAllByMovieTitleAndRoomNameAndStartingTime("testMovieTitle","testRoomName","2021-10-10 16:30");
        verify(bookingRepository).save(new Booking(BOOKING_DTO.getMovieTitle(), BOOKING_DTO.getRoomName(),BOOKING_DTO.getStartingTime(),
                BOOKING_DTO.getSeats(), BOOKING_DTO.getUsername(),3000));
    }

    @Test
    public void testGetBookingListForUserShouldCallScreeningRepositoryAndReturnADtoList() {
        // Given
        Booking booking1= new Booking("testMovieTitle1","testRoomName3","2021-10-15 15:00","10,2","user",1500);
        Booking booking2= new Booking("testMovieTitle2","testRoomName","2021-10-10 16:30","3,4","user",1500);
        BookingDto bookingDto1 = BookingDto.builder()
                .movieTitle("testMovieTitle1")
                .roomName("testRoomName3")
                .startingTime("2021-10-15 15:00")
                .seats("10,2")
                .username("user")
                .price(1500).build();
        BookingDto bookingDto2 = BookingDto.builder()
                .movieTitle("testMovieTitle2")
                .roomName("testRoomName")
                .startingTime("2021-10-10 16:30")
                .seats("3,4")
                .username("user")
                .price(1500).build();
        when(bookingRepository.findAllByUsername("user")).thenReturn(List.of(booking1,booking2));
        List<BookingDto> expected = List.of(bookingDto1,bookingDto2);

        // When
        List<BookingDto> actual = underTest.getBookingListForUser("user");

        // Then
        assertEquals(expected, actual);
        verify(bookingRepository).findAllByUsername("user");
    }

    @Test
    public void testCheckValidSeatsShouldReturnCorrectStringWhenBookingSeatIsInvalid(){

        // Given
        BookingDto bookingDto1 = BookingDto.builder().roomName("testRoomName").seats("4,5 8,10").build();
        BookingDto bookingDto2 = BookingDto.builder().roomName("testRoomName").seats("11,5 10,10").build();
        BookingDto bookingDto3 = BookingDto.builder().roomName("testRoomName").seats("0,7 5,6").build();
        BookingDto bookingDto4 = BookingDto.builder().roomName("testRoomName").seats("4,4 6,0").build();
        RoomDto roomDto = RoomDto.builder()
                .name("testRoomName")
                .rowCount(9)
                .columnCount(9)
                .build();

        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.of(roomDto));
        Optional<String> expected1 = Optional.of("8,10");
        Optional<String> expected2 = Optional.of("11,5");
        Optional<String> expected3 = Optional.of("0,7");
        Optional<String> expected4 = Optional.of("6,0");

        // When
        Optional<String> actual1 = underTest.checkValidSeats(bookingDto1);
        Optional<String> actual2 = underTest.checkValidSeats(bookingDto2);
        Optional<String> actual3 = underTest.checkValidSeats(bookingDto3);
        Optional<String> actual4 = underTest.checkValidSeats(bookingDto4);

        // Then
        verify(roomService, times(4)).getRoomByName("testRoomName");
        assertEquals(expected1,actual1);
        assertEquals(expected2,actual2);
        assertEquals(expected3,actual3);
        assertEquals(expected4,actual4);

    }

    @Test
    public void testGetPriceForBookingShouldReturnOptionalEmptyWhenBookingIsInvalid(){
        // Given
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.empty());
        Optional<Integer> expected= Optional.empty();

        // When
        Optional<Integer> actual = underTest.getPriceForBooking(BOOKING_DTO);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(SCREENING_KEY_ENTITY);
    }

    @Test
    public void testGetPriceForBookingShouldReturnBasePriceWhenBookingIsValidAndThereIsNoChangeInPrice(){
        // Given
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(SCREENING_DTO));
        Optional<Integer> expected= Optional.of(3000);

        // When
        Optional<Integer> actual = underTest.getPriceForBooking(BOOKING_DTO);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(SCREENING_KEY_ENTITY);
    }

    @Test
    public void testGetPriceForBookingShouldReturnCorrectPriceWhenBookingIsValidAndThereIsChangeInPriceForMovie(){
        // Given
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(SCREENING_DTO));
        when(priceComponentService.getChangeInPriceForMovie("testMovieTitle")).thenReturn(-100);
        when(priceComponentService.getChangeInPriceForRoom("testRoomName")).thenReturn(0);
        when(priceComponentService.getChangeInPriceForScreening("testMovieTitle","testRoomName","2021-10-10 16:30")).thenReturn(0);
        Optional<Integer> expected= Optional.of(2800);

        // When
        Optional<Integer> actual = underTest.getPriceForBooking(BOOKING_DTO);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(SCREENING_KEY_ENTITY);
        verify(priceComponentService).getChangeInPriceForMovie("testMovieTitle");
    }

    @Test
    public void testGetPriceForBookingShouldReturnCorrectPriceWhenBookingIsValidAndThereIsChangeInPriceForRoom(){
        // Given
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(SCREENING_DTO));
        when(priceComponentService.getChangeInPriceForMovie("testMovieTitle")).thenReturn(0);
        when(priceComponentService.getChangeInPriceForRoom("testRoomName")).thenReturn(150);
        when(priceComponentService.getChangeInPriceForScreening("testMovieTitle","testRoomName","2021-10-10 16:30")).thenReturn(0);
        Optional<Integer> expected= Optional.of(3300);

        // When
        Optional<Integer> actual = underTest.getPriceForBooking(BOOKING_DTO);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(SCREENING_KEY_ENTITY);
        verify(priceComponentService).getChangeInPriceForRoom("testRoomName");
    }

    @Test
    public void testGetPriceForBookingShouldReturnCorrectPriceWhenBookingIsValidAndThereIsChangeInPriceForScreening(){
        // Given
        when(screeningService.getScreeningByKey(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(SCREENING_DTO));
        when(priceComponentService.getChangeInPriceForMovie("testMovieTitle")).thenReturn(0);
        when(priceComponentService.getChangeInPriceForRoom("testRoomName")).thenReturn(0);
        when(priceComponentService.getChangeInPriceForScreening("testMovieTitle","testRoomName","2021-10-10 16:30")).thenReturn(500);
        Optional<Integer> expected= Optional.of(4000);

        // When
        Optional<Integer> actual = underTest.getPriceForBooking(BOOKING_DTO);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(SCREENING_KEY_ENTITY);
        verify(priceComponentService).getChangeInPriceForScreening("testMovieTitle","testRoomName","2021-10-10 16:30");
    }

}
