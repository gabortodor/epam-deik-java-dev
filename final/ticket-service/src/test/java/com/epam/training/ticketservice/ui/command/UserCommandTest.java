package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.impl.BookingServiceImpl;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserCommandTest {

    private final UserService userService = mock(UserServiceImpl.class);
    private final BookingService bookingService = mock(BookingServiceImpl.class);

    private final UserCommand underTest = new UserCommand(userService, bookingService);

    private final UserDto USER_DTO = new UserDto("user", Role.USER);
    private final UserDto ADMIN_DTO = new UserDto("admin", Role.ADMIN);
    private final BookingDto BOOKING_DTO = BookingDto.builder()
            .movieTitle("testMovieTitle")
            .roomName("testRoomName")
            .startingTime("2021-10-10 16:30")
            .seats("5,5 5,6")
            .username("user")
            .price(3000)
            .build();

    @Test
    public void testSignUpUserShouldReturnCorrectMessageWhenUsernameAndPasswordAreValid() {
        // Given
        doNothing().when(userService).signUp("user", "pass");
        String expected = "Successful sign up!";

        // When
        String actual = underTest.signUpUser("user", "pass");

        //Then
        assertEquals(expected, actual);
        verify(userService).signUp("user", "pass");
    }

    @Test
    public void testSignUpUserShouldReturnCorrectMessageWhenUsernameIsInvalid() {
        // Given
        doThrow(NullPointerException.class).when(userService).signUp(null, "pass");
        String expected = "Sign up failed!";

        // When
        String actual = underTest.signUpUser(null, "pass");

        //Then
        assertEquals(expected, actual);
        verify(userService).signUp(null, "pass");
    }

    @Test
    public void testSignUpUserShouldReturnCorrectMessageWhenPasswordIsInvalid() {
        // Given
        doThrow(NullPointerException.class).when(userService).signUp("user", null);
        String expected = "Sign up failed!";

        // When
        String actual = underTest.signUpUser("user", null);

        //Then
        assertEquals(expected, actual);
        verify(userService).signUp("user", null);
    }

    @Test
    public void testSignInUserShouldReturnCorrectMessageWhenCredentialsAreValid() {
        // Given
        when(userService.signIn("user", "pass")).thenReturn(Optional.of(USER_DTO));
        String expected = "Welcome user!";

        // When
        String actual = underTest.signInUser("user", "pass");

        //Then
        assertEquals(expected, actual);
        verify(userService).signIn("user", "pass");
    }

    @Test
    public void testSignInUserShouldReturnCorrectMessageWhenCredentialsAreInvalid() {
        // Given
        when(userService.signIn("user", "wrongPass")).thenReturn(Optional.empty());
        String expected = "Login failed due to incorrect credentials";

        // When
        String actual = underTest.signInUser("user", "wrongPass");

        //Then
        assertEquals(expected, actual);
        verify(userService).signIn("user", "wrongPass");
    }

    @Test
    public void testSignInPrivilegedShouldReturnCorrectMessageWhenCredentialsAreValid() {
        // Given
        when(userService.signInPrivileged("admin", "admin")).thenReturn(Optional.of(ADMIN_DTO));
        String expected = "Welcome admin!";

        // When
        String actual = underTest.signInPrivileged("admin", "admin");

        //Then
        assertEquals(expected, actual);
        verify(userService).signInPrivileged("admin", "admin");
    }

    @Test
    public void testSignInPrivilegedShouldReturnCorrectMessageWhenCredentialsAreInvalid() {
        // Given
        when(userService.signInPrivileged("admin", "wrongPass")).thenReturn(Optional.empty());
        String expected = "Login failed due to incorrect credentials";

        // When
        String actual = underTest.signInPrivileged("admin", "wrongPass");

        //Then
        assertEquals(expected, actual);
        verify(userService).signInPrivileged("admin", "wrongPass");
    }

    @Test
    public void testDescribeCurrentUserShouldReturnErrorMessageWhenUserIsNotSignedIn() {
        // Given
        when(userService.getSignedInUser()).thenReturn(Optional.empty());
        String expected = "You are not signed in";

        // When
        String actual = underTest.describeCurrentUser();

        //Then
        assertEquals(expected, actual);
        verify(userService).getSignedInUser();
    }

    @Test
    public void testDescribeCurrentUserShouldReturnCorrectMessageWhenUserHasAdminRole() {
        // Given
        when(userService.getSignedInUser()).thenReturn(Optional.of(ADMIN_DTO));
        String expected = "Signed in with privileged account 'admin'";

        // When
        String actual = underTest.describeCurrentUser();

        //Then
        assertEquals(expected, actual);
        verify(userService).getSignedInUser();
    }

    @Test
    public void testDescribeCurrentUserShouldReturnCorrectMessageWhenUserHasUserRoleAndHasNoBookings() {
        // Given
        when(userService.getSignedInUser()).thenReturn(Optional.of(USER_DTO));
        when(bookingService.getBookingListForUser("user")).thenReturn(List.of());
        String expected = "Signed in with account 'user'\n" +
                "You have not booked any tickets yet";

        // When
        String actual = underTest.describeCurrentUser();

        //Then
        assertEquals(expected, actual);
        verify(userService).getSignedInUser();
        verify(bookingService).getBookingListForUser("user");
    }

    @Test
    public void testDescribeCurrentUserShouldReturnCorrectMessageAndListBookingsWhenUserHasUserRoleAndHasBookings() {
        // Given
        when(userService.getSignedInUser()).thenReturn(Optional.of(USER_DTO));
        when(bookingService.getBookingListForUser("user")).thenReturn(List.of(BOOKING_DTO));
        String expected = "Signed in with account 'user'\n" +
                "Your previous bookings are\n" +
                "Seats (5,5), (5,6) on testMovieTitle in room testRoomName starting at 2021-10-10 16:30 for 3000 HUF\n";

        // When
        String actual = underTest.describeCurrentUser();

        //Then
        assertEquals(expected, actual);
        verify(userService).getSignedInUser();
        verify(bookingService).getBookingListForUser("user");
    }

    @Test
    public void testSignOutShouldReturnCorrectMessageWhenThereIsAUserSignedIn() {
        // Given
        when(userService.signOut()).thenReturn(Optional.of(USER_DTO));
        String expected = "user is now signed out!";

        // When
        String actual = underTest.signOut();

        //Then
        assertEquals(expected, actual);
        verify(userService).signOut();
    }

    @Test
    public void testSignOutShouldReturnCorrectMessageWhenThereIsNoUserSignedIn() {
        // Given
        when(userService.signOut()).thenReturn(Optional.empty());
        String expected = "You need to login first!";

        // When
        String actual = underTest.signOut();

        //Then
        assertEquals(expected, actual);
        verify(userService).signOut();
    }

}
