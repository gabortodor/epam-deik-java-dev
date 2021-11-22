package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto,"Room cannot be null");
        Objects.requireNonNull(roomDto.getName(),"The room's name cannot be null");
        Objects.requireNonNull(roomDto.getRowCount(),"The room's row count cannot be null");
        Objects.requireNonNull(roomDto.getColumnCount(),"The room's column count cannot be null");
        Room room = new Room(roomDto.getName(), roomDto.getRowCount(), roomDto.getColumnCount());
        roomRepository.save(room);

    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto,"Room cannot be null");
        Objects.requireNonNull(roomDto.getName(),"The room's name cannot be null");
        Objects.requireNonNull(roomDto.getRowCount(),"The room's row count cannot be null");
        Objects.requireNonNull(roomDto.getColumnCount(),"The room's column count cannot be null");
        Room room = new Room(roomDto.getName(), roomDto.getRowCount(), roomDto.getColumnCount());
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(String name) {
        roomRepository.deleteById(name);

    }

    @Override
    public List<RoomDto> listRooms() {
        return roomRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<RoomDto> getRoomByName(String name) {
        return convertEntityToDto(roomRepository.findById(name));
    }

    private RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
                .name(room.getName())
                .rowCount(room.getRowCount())
                .columnCount(room.getColumnCount())
                .build();
    }

    private Optional<RoomDto> convertEntityToDto(Optional<Room> room) {
        return room.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(room.get()));
    }
}
