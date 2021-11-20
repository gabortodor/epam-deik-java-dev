package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
public class RoomCommand {

    private final RoomService roomService;

    private final UserService userService;

    @Autowired
    public RoomCommand(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Creates the specified room")
    public String createRoom(String name, Integer rowCount, Integer columnCount) {
        roomService.createRoom(name,rowCount,columnCount);
        return name + " was successfully created!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Updates the specified room")
    public String updateRoom(String name, Integer rowCount, Integer columnCount) {
        roomService.updateRoom(name,rowCount,columnCount);
        return name + " was successfully updated!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Deletes the specified room")
    public String deleteRoom(String name) {
        roomService.deleteRoom(name);
        return name + "was successfully deleted!";
    }

    @ShellMethod(key = "list rooms", value = "Lists all the rooms")
    public String listRooms() {
        List<Room> roomList = roomService.listRooms();
        if (roomList.isEmpty()) {
            return "There are no rooms at the moment";
        }
        return roomList.stream().map(r -> new RoomDto(r.getName(), r.getRowCount(), r.getColumnCount()).toString()).collect(Collectors.joining("\n"));
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getSignedInUser();
        if (user.isPresent() && user.get().getRole() == Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("it is only available for users with ADMIN role.");
    }
}
