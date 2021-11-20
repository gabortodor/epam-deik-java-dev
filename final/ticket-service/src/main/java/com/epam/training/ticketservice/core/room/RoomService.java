package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;

import java.util.List;

public interface RoomService {

    void createRoom(String name, Integer rowCount, Integer columnCount);

    void updateRoom(String name, Integer rowCount, Integer columnCount);

    void deleteRoom(String name);

    List<Room> listRooms();
}
