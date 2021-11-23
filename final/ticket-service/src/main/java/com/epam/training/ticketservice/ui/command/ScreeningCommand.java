package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
public class ScreeningCommand {

    private final ScreeningService screeningService;

    private final UserService userService;

    private final MovieService movieService;

    @Autowired
    public ScreeningCommand(ScreeningService screeningService, UserService userService, MovieService movieService) {
        this.screeningService = screeningService;
        this.userService = userService;
        this.movieService = movieService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Creates the specified screening")
    public String createScreening(String movieTitle, String roomName, String startingTime) {
        ScreeningDto screening = ScreeningDto.builder()
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startingTime(startingTime).build();
        return screeningService.createScreening(screening);
    }


    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Deletes the specified screening")
    public String deleteScreening(String movieTitle, String roomName, String startingTime) {
        ScreeningDto screening = ScreeningDto.builder()
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startingTime(startingTime).build();
        screeningService.deleteScreening(screening);
        return "The screening was successfully deleted!";
    }

    @ShellMethod(key = "list screenings", value = "Lists all the screenings")
    public String listScreenings() {
        List<ScreeningDto> screeningList = screeningService.getScreeningList();
        if (screeningList.isEmpty()) {
            return "There are no screenings";
        }
        List<String> screeningStrings = screeningList.stream().map(s -> s.getMovieTitle()
                + " (" + movieService.getMovieByTitle(s.getMovieTitle()).get().getGenre()
                + ", " + movieService.getMovieByTitle(s.getMovieTitle()).get().getRuntime()
                + " minutes), screened in room " + s.getRoomName() + ", at "
                + s.getStartingTime()).collect(Collectors.toList());

        //TODO
        Locale hungarian = new Locale("hu", "HU");
        Collator collator = Collator.getInstance(hungarian);
        Collections.sort(screeningStrings, collator);
        return String.join("\n",screeningStrings);
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getSignedInUser();
        if (user.isPresent() && user.get().getRole() == Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("it is only available for users with ADMIN role.");
    }
}
