package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
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
public class MovieCommand {

    private final MovieService movieService;

    private final UserService userService;

    @Autowired
    public MovieCommand(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Creates the specified movie")
    public String createMovie(String title, String genre, Integer runtime) {
        movieService.createMovie(title, genre, runtime);
        return title + " was successfully created!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Updates the specified movie")
    public String updateMovie(String title, String genre, Integer runtime) {
        movieService.updateMovie(title, genre, runtime);
        return title + " was successfully updated!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Deletes the specified movie")
    public String deleteMovie(String title) {
        movieService.deleteMovie(title);
        return title + "was successfully deleted!";
    }

    @ShellMethod(key = "list movies", value = "Lists all the movies")
    public String listMovies() {
        List<Movie> movieList = movieService.listMovies();
        if (movieList.isEmpty()) {
            return "There are no movies at the moment";
        }
        return movieList.stream().map(m -> new MovieDto(m.getTitle(), m.getGenre(), m.getRuntime()).toString()).collect(Collectors.joining("\n"));
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getSignedInUser();
        if (user.isPresent() && user.get().getRole() == Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("it is only available for users with ADMIN role.");
    }
}
