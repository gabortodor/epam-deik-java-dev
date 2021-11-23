package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private UserService underTest = new UserServiceImpl(userRepository);


    @Test
    public void testSignInShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.signIn(null, "pass"));
    }

    @Test
    public void testSignInShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.signIn("user", null));
    }

    @Test
    public void testSignInShouldSetSignedInUserWhenUsernameAndPasswordAreCorrect() {
        // Given
        User user = new User("user", "password", Role.USER);
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPasswordAndRole("user", "pass", Role.USER)).thenReturn(Optional.of(user));

        // When
        Optional<UserDto> actual = underTest.signIn("user", "pass");

        // Then
        assertEquals(expected.get().getUsername(), actual.get().getUsername());
        assertEquals(expected.get().getRole(), actual.get().getRole());
        verify(userRepository).findByUsernameAndPasswordAndRole("user", "pass", Role.USER);
    }

    @Test
    public void testSignInShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        // Given
        Optional<UserDto> expected = Optional.empty();
        when(userRepository.findByUsernameAndPasswordAndRole("dummy", "dummy",Role.USER)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> actual = underTest.signIn("dummy", "dummy");

        // Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPasswordAndRole("dummy", "dummy", Role.USER);
    }

    @Test
    public void testSignInPrivilegedShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.signInPrivileged(null, "admin"));
    }

    @Test
    public void testSignInPrivilegedShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.signInPrivileged("admin", null));
    }

    @Test
    public void testSignInPrivilegedShouldSetSignedInUserWhenUsernameAndPasswordAreCorrect() {
        // Given
        User user = new User("admin", "admin", Role.ADMIN);
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPasswordAndRole("admin", "admin", Role.ADMIN)).thenReturn(Optional.of(user));

        // When
        Optional<UserDto> actual = underTest.signInPrivileged("admin", "admin");

        // Then
        assertEquals(expected.get().getUsername(), actual.get().getUsername());
        assertEquals(expected.get().getRole(), actual.get().getRole());
        verify(userRepository).findByUsernameAndPasswordAndRole("admin", "admin", Role.ADMIN);
    }

    @Test
    public void testSignInPrivilegedShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        // Given
        Optional<UserDto> expected = Optional.empty();
        when(userRepository.findByUsernameAndPasswordAndRole("admin", "admin",Role.ADMIN)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> actual = underTest.signInPrivileged("admin", "admin");

        // Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPasswordAndRole("admin", "admin", Role.ADMIN);
    }

    @Test
    public void testSignOutShouldReturnOptionalEmptyWhenThereIsNoOneSignedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.signOut();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testSignOutShouldReturnThePreviouslyLoggedInUserWhenThereIsASignedInUser() {
        // Given
        UserDto user = new UserDto("user", Role.USER);
        Optional<UserDto> expected = Optional.of(user);
        underTest = new UserServiceImpl(user, userRepository);

        // When
        Optional<UserDto> actual = underTest.signOut();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSignedInUserShouldReturnTheSignedInUserWhenThereIsASignedInUser() {
        // Given
        UserDto user = new UserDto("user", Role.USER);
        Optional<UserDto> expected = Optional.of(user);
        underTest = new UserServiceImpl(user, userRepository);

        // When
        Optional<UserDto> actual = underTest.getSignedInUser();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSignedInUserShouldReturnOptionalEmptyWhenThereIsNoOneSignedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.getSignedInUser();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testSignUpShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.signUp(null, "pass"));
    }

    @Test
    public void testSignUpShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.signUp("user", null));
    }

    @Test
    public void testRegisterUserShouldCallUserRepositoryWhenTheInputIsValid() {
        // Given
        // When
        underTest.signUp("user", "pass");

        // Then
        verify(userRepository).save(new User("user", "pass", Role.USER));
    }
}
