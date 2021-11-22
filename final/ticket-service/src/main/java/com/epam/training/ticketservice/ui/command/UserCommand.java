package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@ShellComponent
public class UserCommand {

    private final UserService userService;
    private final BookingService bookingService;

    @Autowired
    public UserCommand(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @ShellMethod(key = "sign up", value = "Registers a user, with the specified username and password")
    public String signUpUser(String username, String password) {
        try {
            userService.signUp(username, password);
            return "Successful sign up!";
        } catch (Exception e) {
            return "Sign up failed!";
        }
    }

    @ShellMethod(key = "sign in", value = "User login")
    public String signInUser(String username, String password) {
        Optional<UserDto> user = userService.signIn(username, password);
        return user.isEmpty() ? "Login failed due to incorrect credentials" :
                "Welcome " + user.get().getUsername() + "!";
    }

    @ShellMethod(key = "sign in privileged", value = "Admin login")
    public String signInPrivileged(String username, String password) {
        Optional<UserDto> user = userService.signInPrivileged(username, password);
        return user.isEmpty() ? "Login failed due to incorrect credentials" :
                "Welcome " + user.get().getUsername() + "!";
    }

    @ShellMethod(key = "describe account", value = "Describes currently logged in account")
    public String describeCurrentUser() {
        Optional<UserDto> userDto = userService.getSignedInUser();
        if (userDto.isEmpty()) {
            return "You are not signed in";
        }
        UserDto user = userDto.get();
        if (user.getRole().equals(Role.ADMIN)) {
            return "Signed in with privileged account 'admin'";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Signed in with account '" + user.getUsername() + "'\n");
        List<BookingDto> bookings = bookingService.listBookingForUser(user.getUsername());

        if (bookings.isEmpty()) {
            sb.append("You have not booked any tickets yet");
        } else {
            sb.append("Your previous bookings are\n");
            for (BookingDto booking : bookings) {
                sb.append(booking.toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public String signOut() {
        Optional<UserDto> user = userService.signOut();
        return user.isEmpty() ? "You need to login first!" : user.get().getUsername() + " is now signed out!";
    }
}
