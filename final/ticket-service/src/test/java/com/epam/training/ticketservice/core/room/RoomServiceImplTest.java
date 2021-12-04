package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomServiceImplTest {

    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final RoomService underTest = new RoomServiceImpl(roomRepository);


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
    public void testCreateRoomShouldCallRoomRepositoryWhenTheInputIsValid() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name("testName")
                .rowCount(10)
                .columnCount(12)
                .build();
        // When
        underTest.createRoom(roomDto);

        // Then
        verify(roomRepository).save(new Room("testName", 10, 12));
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

    @Test
    public void testUpdateRoomShouldCallRoomRepositoryWhenTheInputIsValid() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name("testName")
                .rowCount(10)
                .columnCount(12)
                .build();
        when(roomRepository.findById("testName")).thenReturn(Optional.of(new Room("testName", 10,12)));
        // When
        underTest.updateRoom(roomDto);

        // Then
        verify(roomRepository).save(new Room("testName", 10, 12));
    }

    @Test
    public void testDeleteRoomShouldCallRoomRepository() {
        //Given-When
        underTest.deleteRoom("testName");

        // Then
        verify(roomRepository).deleteById("testName");
    }

    @Test
    public void testGetRoomListShouldCallRoomRepositoryAndReturnADtoList() {
        // Given
        Room room1= new Room("testName1",10,12);
        Room room2= new Room("testName2",22,20);
        RoomDto roomDto1 = RoomDto.builder().name("testName1").rowCount(10).columnCount(12).changeInPrice(0).build();
        RoomDto roomDto2 = RoomDto.builder().name("testName2").rowCount(22).columnCount(20).changeInPrice(0).build();
        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));
        List<RoomDto> expected = List.of(roomDto1, roomDto2);

        // When
        List<RoomDto> actual = underTest.getRoomList();

        // Then
        assertEquals(expected, actual);
        verify(roomRepository).findAll();
    }

    @Test
    public void testGetRoomByNameShouldReturnOptionalEmptyWhenInputRoomNameDoesNotExist() {
        // Given
        when(roomRepository.findById("testName")).thenReturn(Optional.empty());
        Optional<RoomDto> expected = Optional.empty();

        // When
        Optional<RoomDto> actual = underTest.getRoomByName("testName");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(roomRepository).findById("testName");
    }

    @Test
    public void testGetRoomByNameShouldReturnRoomWhenInputRoomNameIsValid() {
        // Given
        Room room = new Room("testName", 10, 12);
        RoomDto roomDto = RoomDto.builder().name("testName").rowCount(10).columnCount(12).changeInPrice(0).build();
        when(roomRepository.findById("testName")).thenReturn(Optional.of(room));
        Optional<RoomDto> expected = Optional.of(roomDto);

        // When
        Optional<RoomDto> actual = underTest.getRoomByName("testName");

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
        verify(roomRepository).findById("testName");
    }

    @Test
    public void testUpdateChangeInPriceShouldReturnMessageIfGivenRoomIsNotValid() {
        // Given
        when(roomRepository.findById("testName")).thenReturn(Optional.empty());
        String expected = "No such room exists";

        // When
        String actual = underTest.updateChangeInPrice("testName",200);

        // Then
        assertEquals(expected, actual);
        verify(roomRepository).findById("testName");
    }

    @Test
    public void testUpdateChangeInPriceShouldSetChangeInPriceAndCallRoomRepositoryAndReturnMessageIfGivenRoomIsValid() {
        // Given
        when(roomRepository.findById("testName")).thenReturn(Optional.of(new Room("testName",10,12)));
        String expected = "Price component successfully applied";

        // When
        String actual = underTest.updateChangeInPrice("testName",200);

        // Then
        assertEquals(expected, actual);
        verify(roomRepository).save(new Room("testName",10,12,200));
    }
}
