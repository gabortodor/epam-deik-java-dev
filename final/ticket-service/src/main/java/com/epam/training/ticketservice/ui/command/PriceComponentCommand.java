package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
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
public class PriceComponentCommand {

    private final BookingService bookingService;

    private final PriceComponentService priceComponentService;

    private final UserService userService;

    @Autowired
    public PriceComponentCommand(PriceComponentService priceComponentService,
                                 UserService userService, BookingService bookingService) {
        this.priceComponentService = priceComponentService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update base price", value = "Updates the base price")
    public String updateBasePrice(Integer newBasePrice) {
        bookingService.setBasePrice(newBasePrice);
        return "The base price was successfully updated";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create price component", value = "Creates the specified price component")
    public String createPriceComponent(String name, Integer value) {
        PriceComponentDto priceComponentDto = PriceComponentDto.builder().name(name).value(value).build();
        priceComponentService.createPriceComponent(priceComponentDto);
        return name + " was successfully created";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to room",
            value = "Attaches a specified price component to the specified room")
    public String attachPriceComponentToRoom(String name, String roomName) {
        return priceComponentService.attachPriceComponentToRoom(name, roomName);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to movie",
            value = "Attaches a specified price component to the specified movie")
    public String attachPriceComponentToMovie(String name, String movieTitle) {
        return priceComponentService.attachPriceComponentToMovie(name, movieTitle);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to screening",
            value = "Attaches a specified price component to the specified screening")
    public String attachPriceComponentToScreening(String name, String movieTitle,
                                                  String roomName, String startingTime) {
        return priceComponentService.attachPriceComponentToScreening(name, movieTitle, roomName, startingTime);
    }

    @ShellMethod(key = "show price for",
            value = "Shows the price for a specified booking without actually booking it")
    public String showPriceForBooking(String movieTitle, String roomName, String startingTime, String seats) {
        BookingDto booking = BookingDto.builder()
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startingTime(startingTime)
                .seats(seats).build();
        int price = bookingService.getPriceForBooking(booking).orElseThrow(()
                -> new IllegalStateException("Invalid booking"));
        return "The price for this booking would be " + price + " HUF";
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getSignedInUser();
        if (user.isPresent() && user.get().getRole() == Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("it is only available for users with ADMIN role.");
    }
}
