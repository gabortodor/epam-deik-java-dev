package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScreeningServiceImplTest {

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

    private final RoomDto ROOM_DTO = RoomDto.builder()
            .name("testRoomName")
            .rowCount(10)
            .rowCount(12)
            .build();

    private final Screening SCREENING_ENTITY_1 = new Screening("testMovieTitle","testRoomName","2021-10-11 15:00");
    private final Screening SCREENING_ENTITY_2 = new Screening("testMovieTitle","testRoomName","2021-10-10 14:00");

    private final Screening.ScreeningKey SCREENING_KEY_ENTITY = new Screening.ScreeningKey("testMovieTitle","testRoomName","2021-10-10 16:30");


    private final ScreeningRepository screeningRepository  = mock(ScreeningRepository.class);

    private final RoomService roomService = mock(RoomServiceImpl.class);
    private final MovieService movieService = mock(MovieServiceImpl.class);

    private final ScreeningService underTest = new ScreeningServiceImpl(screeningRepository,movieService,roomService);

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

    @Test
    public void testCreateScreeningShouldCallMovieServiceAndReturnStringMessageWhenInputMovieDoesNotExist(){
        // Given
        when(movieService.getMovieByTitle("testMovieTitle")).thenReturn(Optional.empty());
        String expected = "No movie can be found with the specified title";

        // When
        String actual = underTest.createScreening(SCREENING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(movieService).getMovieByTitle("testMovieTitle");
    }

    @Test
    public void testCreateScreeningShouldCallRoomServiceAndReturnStringMessageWhenInputRoomDoesNotExist(){
        // Given
        when(movieService.getMovieByTitle("testMovieTitle")).thenReturn(Optional.of(MOVIE_DTO));
        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.empty());
        String expected = "No room can be found with the specified name";

        // When
        String actual = underTest.createScreening(SCREENING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(movieService, times(2)).getMovieByTitle("testMovieTitle");
        verify(roomService).getRoomByName("testRoomName");
    }
    @Test
    public void testCreateScreeningShouldReturnStringMessageWhenThereIsAnOverlappingScreeningWithInputScreening(){
        // Given
        Screening screening2 = new Screening("testMovieTitle","testRoomName","2021-10-10 16:00");

        when(movieService.getMovieByTitle("testMovieTitle")).thenReturn(Optional.of(MOVIE_DTO));
        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.of(ROOM_DTO));
        when(screeningRepository.findAllByRoomName("testRoomName")).thenReturn(List.of(SCREENING_ENTITY_1, screening2));
        String expected = "There is an overlapping screening";

        // When
        String actual = underTest.createScreening(SCREENING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(movieService, times(4)).getMovieByTitle("testMovieTitle");
        verify(roomService).getRoomByName("testRoomName");
        verify(screeningRepository).findAllByRoomName("testRoomName");
    }

    @Test
    public void testCreateScreeningShouldReturnStringMessageWhenTheInputScreeningWouldStartInAnOtherScreeningsBreakPeriod(){
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("testMovieTitle")
                .roomName("testRoomName")
                .startingTime("2021-10-10 16:05")
                .changeInPrice(0)
                .build();

        when(movieService.getMovieByTitle("testMovieTitle")).thenReturn(Optional.of(MOVIE_DTO));
        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.of(ROOM_DTO));
        when(screeningRepository.findAllByRoomName("testRoomName")).thenReturn(List.of(SCREENING_ENTITY_1, SCREENING_ENTITY_2));
        String expected = "This would start in the break period after another screening in this room";

        // When
        String actual = underTest.createScreening(screeningDto);

        //Then
        assertEquals(expected, actual);
        verify(movieService, times(4)).getMovieByTitle("testMovieTitle");
        verify(roomService).getRoomByName("testRoomName");
        verify(screeningRepository).findAllByRoomName("testRoomName");
    }

    @Test
    public void testCreateScreeningShouldReturnStringMessageAndCallScreeningRepositoryWhenTheInputScreeningIsValidAndNotOverlaps(){
        // Given
        when(movieService.getMovieByTitle("testMovieTitle")).thenReturn(Optional.of(MOVIE_DTO));
        when(roomService.getRoomByName("testRoomName")).thenReturn(Optional.of(ROOM_DTO));
        when(screeningRepository.findAllByRoomName("testRoomName")).thenReturn(List.of(SCREENING_ENTITY_1, SCREENING_ENTITY_2));
        String expected = "Screening successfully created!";

        // When
        String actual = underTest.createScreening(SCREENING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(movieService, times(4)).getMovieByTitle("testMovieTitle");
        verify(roomService).getRoomByName("testRoomName");
        verify(screeningRepository).findAllByRoomName("testRoomName");
        verify(screeningRepository).save(new Screening("testMovieTitle", "testRoomName","2021-10-10 16:30"));
    }

    @Test
    public void testDeleteScreeningShouldCallScreeningRepository() {
        //Given
        Screening.ScreeningKey screeningKey = new Screening.ScreeningKey("testMovieTitle", "testRoomName", "2021-10-10 16:30");


        // When
        underTest.deleteScreening(SCREENING_DTO);

        // Then
        verify(screeningRepository).deleteById(screeningKey);
    }

    @Test
    public void testGetScreeningListShouldCallScreeningRepositoryAndReturnADtoList() {
        // Given
        ScreeningDto screeningDto1 = ScreeningDto.builder().movieTitle("testMovieTitle").roomName("testRoomName").startingTime("2021-10-11 15:00").changeInPrice(0).build();
        ScreeningDto screeningDto2 = ScreeningDto.builder().movieTitle("testMovieTitle").roomName("testRoomName").startingTime("2021-10-10 14:00").changeInPrice(0).build();
        when(screeningRepository.findAll()).thenReturn(List.of(SCREENING_ENTITY_1, SCREENING_ENTITY_2));
        List<ScreeningDto> expected = List.of(screeningDto1, screeningDto2);

        // When
        List<ScreeningDto> actual = underTest.getScreeningList();

        // Then
        assertEquals(expected, actual);
        verify(screeningRepository).findAll();
    }

    @Test
    public void testGetScreeningByKeyShouldReturnScreeningWhenInputKeyIsValid() {
        // Given
        Screening screening = new Screening("testMovieTitle","testRoomName","2021-10-10 16:30");
        ScreeningDto screeningDto = ScreeningDto.builder().movieTitle("testMovieTitle").roomName("testRoomName").startingTime("2021-10-10 16:30").changeInPrice(0).build();
        when(screeningRepository.findById(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(screening));
        Optional<ScreeningDto> expected = Optional.of(screeningDto);

        // When
        Optional<ScreeningDto> actual = underTest.getScreeningByKey(SCREENING_KEY_ENTITY);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
        verify(screeningRepository).findById(SCREENING_KEY_ENTITY);
    }

    @Test
    public void testUpdateChangeInPriceShouldReturnMessageIfGivenRoomIsNotValid() {
        // Given
        when(screeningRepository.findById(SCREENING_KEY_ENTITY)).thenReturn(Optional.empty());
        String expected = "No such screening exists";

        // When
        String actual = underTest.updateChangeInPrice("testMovieTitle","testRoomName","2021-10-10 16:30",200);

        // Then
        assertEquals(expected, actual);
        verify(screeningRepository).findById(SCREENING_KEY_ENTITY);
    }

    @Test
    public void testUpdateChangeInPriceShouldSetChangeInPriceAndCallRoomRepositoryAndReturnMessageIfGivenRoomIsValid() {
        // Given
        when(screeningRepository.findById(SCREENING_KEY_ENTITY)).thenReturn(Optional.of(new Screening("testMovieTitle","testRoomName","2021-10-10 16:30")));
        String expected = "Price component successfully applied";

        // When
        String actual = underTest.updateChangeInPrice("testMovieTitle","testRoomName","2021-10-10 16:30",200);

        // Then
        assertEquals(expected, actual);
        verify(screeningRepository).save(new Screening("testMovieTitle","testRoomName","2021-10-10 16:30", 200));
    }


}
