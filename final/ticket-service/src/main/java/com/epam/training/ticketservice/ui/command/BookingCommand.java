package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class BookingCommand {

    private final BookingService bookingService;

    private final UserService userService;

    @Autowired
    public BookingCommand(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "book", value = "Creates the specified booking")
    public String createBooking(String movieTitle, String roomName, String startingTime, String seats) {
        String username = userService.getSignedInUser().get().getUsername();
        BookingDto bookingDto = BookingDto.builder().movieTitle(movieTitle)
                .roomName(roomName).startingTime(startingTime)
                .seats(seats).username(username).build();
        return bookingService.createBooking(bookingDto);
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getSignedInUser();
        if (user.isPresent() && user.get().getRole() == Role.USER) {
            return Availability.available();
        }
        return Availability.unavailable("it is only available for users with USER role.");
    }
}
