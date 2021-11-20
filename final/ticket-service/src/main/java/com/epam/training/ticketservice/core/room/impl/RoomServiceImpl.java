package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void createRoom(String name, Integer rowCount, Integer columnCount) {
        Room room = new Room(name, rowCount, columnCount);
        roomRepository.save(room);

    }

    @Override
    public void updateRoom(String name, Integer rowCount, Integer columnCount) {
        Room room = new Room(name, rowCount, columnCount);
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(String name) {
        Optional<Room> room = roomRepository.findById(name);
        if (room.isPresent())
            roomRepository.delete(room.get());

    }

    @Override
    public List<Room> listRooms() {
        return roomRepository.findAll();
    }
}
