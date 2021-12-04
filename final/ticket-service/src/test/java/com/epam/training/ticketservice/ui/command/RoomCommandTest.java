package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoomCommandTest {

    private final RoomService roomService = mock(RoomServiceImpl.class);
    private final UserService userService = mock(UserServiceImpl.class);

    private final RoomCommand underTest = new RoomCommand(roomService, userService);

    @Test
    public void testLisRoomsShouldReturnCorrectMessageWhenThereAreNoRooms(){
        // Given
        when(roomService.getRoomList()).thenReturn(List.of());
        String expected = "There are no rooms at the moment";

        // When
        String actual = underTest.listRooms();

        // Then
        assertEquals(expected, actual);
        verify(roomService).getRoomList();
    }

    @Test
    public void testListRoomsShouldReturnRoomListWhenThereAreRooms(){
        // Given
        when(roomService.getRoomList()).thenReturn(List.of(RoomDto.builder()
                .name("testRoomName1")
                .rowCount(10)
                .columnCount(12).build(), RoomDto.builder()
                .name("testRoomName2")
                .rowCount(7)
                .columnCount(8).build()));
        String expected =  "Room testRoomName1 with 120 seats, 10 rows and 12 columns\n" +
                "Room testRoomName2 with 56 seats, 7 rows and 8 columns";


        // When
        String actual = underTest.listRooms();

        // Then
        assertEquals(expected, actual);
        verify(roomService).getRoomList();
    }
}
