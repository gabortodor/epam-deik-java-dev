package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
public class UserCommand {

    private final UserService userService;

    @Autowired
    public UserCommand(UserService userService) {
        this.userService = userService;
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
        return user.isEmpty() ? "Login failed due to incorrect credentials" : "Welcome " + user.get().getUsername() + "!";
    }

    @ShellMethod(key = "sign in privileged", value = "Admin login")
    public String signInPrivileged(String username, String password) {
        Optional<UserDto> user = userService.signInPrivileged(username, password);
        return user.isEmpty() ? "Login failed due to incorrect credentials" : "Welcome " + user.get().getUsername() + "!";
    }

    @ShellMethod(key = "describe account", value = "Describes currently logged in account")
    public String describeCurrentUser() {
        Optional<UserDto> userDto = userService.getSignedInUser();
        if (userDto.isEmpty())
            return "You are not signed in";
        UserDto user = userDto.get();
        if (user.getRole().equals(Role.ADMIN)) {
            return "Signed in with privileged account " + user;
        }
        //TODO User information with booking information
        return null;
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public String signOut() {
        Optional<UserDto> user = userService.signOut();
        return user.isEmpty() ? "You need to login first!" : user.get().getUsername() + " is now signed out!";
    }
}
