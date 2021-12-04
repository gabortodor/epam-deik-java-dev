package com.epam.training.ticketservice.core.pricecomponent;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.pricecomponent.impl.PriceComponentServiceImpl;
import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PriceComponentServiceImplTest {

    private final PriceComponentRepository priceComponentRepository = mock(PriceComponentRepository.class);

    private final RoomService roomService = mock(RoomServiceImpl.class);
    private final MovieService movieService = mock(MovieServiceImpl.class);
    private final ScreeningService screeningService = mock(ScreeningServiceImpl.class);

    private final PriceComponentService underTest = new PriceComponentServiceImpl(priceComponentRepository, movieService, roomService, screeningService);


    @Test
    public void testCreatePriceComponentShouldThrowNullPointerExceptionWhenPriceComponentIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createPriceComponent(null));
    }

    @Test
    public void testCreatePriceComponentShouldThrowNullPointerExceptionWhenPriceComponentNameIsNull() {
        // Given
        PriceComponentDto priceComponentDto = PriceComponentDto.builder()
                .name(null)
                .value(0)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createPriceComponent(priceComponentDto));
    }

    @Test
    public void testCreatePriceComponentShouldCallPriceComponentRepositoryWhenTheInputIsValid() {
        // Given
        PriceComponentDto priceComponentDto = PriceComponentDto.builder()
                .name("testName")
                .value(100)
                .build();
        // When
        underTest.createPriceComponent(priceComponentDto);

        // Then
        verify(priceComponentRepository).save(new PriceComponent("testName", 100));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldCallPriceComponentRepositoryAndReturnMessageWhenInputNameIsInvalid(){
        // Given
        when(priceComponentRepository.findById("testComponentName")).thenReturn(Optional.empty());
        String expected = "No price component exists with such name";

        //When
        String actual = underTest.attachPriceComponentToRoom("testComponentName", "testRoomName");

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById("testComponentName");
    }

    @Test
    public void testAttachPriceComponentToRoomShouldCallPriceComponentRepositoryAndRoomServiceAndReturnMessageWhenInputNameIsValid(){
        // Given
        when(priceComponentRepository.findById("testComponentName")).thenReturn(Optional.of(new PriceComponent("testComponentName", 100)));
        when(roomService.updateChangeInPrice("testRoomName", 100)).thenReturn("Price component successfully applied");
        String expected = "Price component successfully applied";

        //When
        String actual = underTest.attachPriceComponentToRoom("testComponentName", "testRoomName");

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById("testComponentName");
        verify(roomService).updateChangeInPrice("testRoomName",100);
    }


    @Test
    public void testAttachPriceComponentToMovieShouldCallPriceComponentRepositoryAndReturnMessageWhenInputNameIsInvalid(){
        // Given
        when(priceComponentRepository.findById("testComponentName")).thenReturn(Optional.empty());
        String expected = "No price component exists with such name";

        //When
        String actual = underTest.attachPriceComponentToMovie("testComponentName", "testTitle");

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById("testComponentName");
    }

    @Test
    public void testAttachPriceComponentToMovieShouldCallPriceComponentRepositoryAndMovieServiceAndReturnMessageWhenInputNameIsValid(){
        // Given
        when(priceComponentRepository.findById("testComponentName")).thenReturn(Optional.of(new PriceComponent("testComponentName", 100)));
        when(movieService.updateChangeInPrice("testMovieTitle", 100)).thenReturn("Price component successfully applied");
        String expected = "Price component successfully applied";

        //When
        String actual = underTest.attachPriceComponentToMovie("testComponentName", "testMovieTitle");

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById("testComponentName");
        verify(movieService).updateChangeInPrice("testMovieTitle",100);
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldCallPriceComponentRepositoryAndReturnMessageWhenInputNameIsInvalid(){
        // Given
        when(priceComponentRepository.findById("testComponentName")).thenReturn(Optional.empty());
        String expected = "No price component exists with such name";

        //When
        String actual = underTest.attachPriceComponentToScreening("testComponentName", "movieTitle", "testRoomName","2021-10-10 16:30");

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById("testComponentName");
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldCallPriceComponentRepositoryAndScreeningServiceAndReturnMessageWhenInputNameIsValid(){
        // Given
        when(priceComponentRepository.findById("testComponentName")).thenReturn(Optional.of(new PriceComponent("testComponentName", 100)));
        when(screeningService.updateChangeInPrice("testMovieTitle", "testRoomName","2021-10-10 16:30", 100)).thenReturn("Price component successfully applied");
        String expected = "Price component successfully applied";

        //When
        String actual = underTest.attachPriceComponentToScreening("testComponentName", "testMovieTitle","testRoomName", "2021-10-10 16:30");

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById("testComponentName");
        verify(screeningService).updateChangeInPrice("testMovieTitle", "testRoomName","2021-10-10 16:30", 100);
    }

    @Test
    public void testGetChangeInPriceForMovieShouldThrowIllegalStateExceptionWhenMovieIsInvalid() {
        // Given
        when(movieService.getMovieByTitle("testMovieTitle")).thenReturn(Optional.empty());

        // When - Then
        assertThrows(IllegalStateException.class, () -> underTest.getChangeInPriceForMovie("testMovieTitle"));
    }

    @Test
    public void testGetChangeInPriceForMovieShouldReturnChangeInPriceWhenMovieIsValid() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("testMovieTitle")
                .genre("comedy")
                .runtime(120)
                .changeInPrice(100)
                .build();

        when(movieService.getMovieByTitle("testMovieTitle")).thenReturn(Optional.of(movieDto));
        int expected = 100;

        // When
        int actual = underTest.getChangeInPriceForMovie("testMovieTitle");

        // Then
        assertEquals(expected, actual);
        verify(movieService).getMovieByTitle("testMovieTitle");
    }

    @Test
    public void testGetChangeInPriceForRoomShouldThrowIllegalStateExceptionWhenRoomIsInvalid() {
        // Given
        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.empty());

        // When - Then
        assertThrows(IllegalStateException.class, () -> underTest.getChangeInPriceForRoom("testRoomName"));
    }

    @Test
    public void testGetChangeInPriceForRoomShouldReturnChangeInPriceWhenRoomIsValid() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name("testRoomName")
                .rowCount(10)
                .columnCount(12)
                .changeInPrice(100)
                .build();

        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.of(roomDto));
        int expected = 100;

        // When
        int actual = underTest.getChangeInPriceForRoom("testRoomName");

        // Then
        assertEquals(expected, actual);
        verify(roomService).getRoomByName("testRoomName");
    }

    @Test
    public void testGetChangeInScreeningForRoomShouldThrowIllegalStateExceptionWhenScreeningIsInvalid() {
        // Given
        Screening.ScreeningKey screeningKey= new Screening.ScreeningKey("testMovieTitle","testRoomName","2021-10-10 16:30");
        when(screeningService.getScreeningByKey(screeningKey)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(IllegalStateException.class, () -> underTest.getChangeInPriceForScreening("testMovieTitle","testRoomName","2021-10-10 16:30"));
    }

    @Test
    public void testGetChangeInPriceForScreeningShouldReturnChangeInPriceWhenScreeningIsValid() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("testMovieTitle")
                .roomName("testRoomName")
                .startingTime("2021-10-10 16:30")
                .changeInPrice(100)
                .build();

        Screening.ScreeningKey screeningKey= new Screening.ScreeningKey("testMovieTitle","testRoomName","2021-10-10 16:30");
        when(screeningService.getScreeningByKey(screeningKey)).thenReturn(Optional.of(screeningDto));
        int expected = 100;

        // When
        int actual = underTest.getChangeInPriceForScreening("testMovieTitle","testRoomName","2021-10-10 16:30");

        // Then
        assertEquals(expected, actual);
        verify(screeningService).getScreeningByKey(screeningKey);
    }
}
