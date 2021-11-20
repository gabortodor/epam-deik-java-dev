package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserDto signedInUser;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public Optional<UserDto> signIn(String username, String password) {
        Objects.requireNonNull(username, "Username must have a value for a successful login!");
        Objects.requireNonNull(password, "Password must have a value for a successful login!");
        signedInUser = retrieveUserInfoByNameAndPassword(username, password, Role.USER);
        return getSignedInUser();
    }

    @Override
    public Optional<UserDto> signInPrivileged(String username, String password){
        Objects.requireNonNull(username, "Username must have a value for a successful login!");
        Objects.requireNonNull(password, "Password must have a value for a successful login!");
        signedInUser = retrieveUserInfoByNameAndPassword(username, password, Role.ADMIN);
        return getSignedInUser();
    }

    @Override
    public Optional<UserDto> signOut() {
        Optional<UserDto> previouslyLoggedInUser = getSignedInUser();
        signedInUser = null;
        return previouslyLoggedInUser;
    }

    @Override
    public Optional<UserDto> getSignedInUser() {
        return Optional.ofNullable(signedInUser);
    }

    @Override
    public void signUp(String username, String password) {
        Objects.requireNonNull(username, "Username must have a value for a successful registration!");
        Objects.requireNonNull(password, "Password must have a value for a successful registration!");
        User user = new User(username, password, Role.USER);
        userRepository.save(user);
    }

    private UserDto retrieveUserInfoByNameAndPassword(String username, String password, Role role) {
        Optional<User> user = userRepository.findByUsernameAndPasswordAndRole(username, password, role);
        if (user.isEmpty()) {
            return null;
        }
        return new UserDto(user.get().getUsername(), user.get().getRole());
    }
}
