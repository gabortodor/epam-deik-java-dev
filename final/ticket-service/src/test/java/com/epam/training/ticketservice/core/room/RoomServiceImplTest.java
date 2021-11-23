package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class RoomServiceImplTest {

    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private RoomService underTest = new RoomServiceImpl(roomRepository);


    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(null));
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name(null)
                .rowCount(10)
                .columnCount(12)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(roomDto));
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomRowCountIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name("test")
                .rowCount(null)
                .columnCount(12)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(roomDto));
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomColumnCountIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name("test")
                .rowCount(10)
                .columnCount(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(roomDto));
    }

    @Test
    public void testUpdateRoomShouldThrowNullPointerExceptionWhenRoomIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(null));
    }

    @Test
    public void testUpdateRoomShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name(null)
                .rowCount(10)
                .columnCount(12)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(roomDto));
    }

    @Test
    public void testUpdateRoomShouldThrowNullPointerExceptionWhenRoomRowCountIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name("test")
                .rowCount(null)
                .columnCount(12)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(roomDto));
    }

    @Test
    public void testUpdateRoomShouldThrowNullPointerExceptionWhenRoomColumnCountIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name("test")
                .rowCount(10)
                .columnCount(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(roomDto));
    }
}
