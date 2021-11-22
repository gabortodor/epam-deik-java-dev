package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;

    private final MovieService movieService;
    private final RoomService roomService;

    @Autowired
    ScreeningServiceImpl(ScreeningRepository screeningRepository, MovieService movieService, RoomService roomService) {
        this.screeningRepository = screeningRepository;
        this.movieService = movieService;
        this.roomService = roomService;
    }


    @Override
    public String createScreening(ScreeningDto screeningDto) {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovieTitle(), "The screening's movie cannot be null");
        Objects.requireNonNull(screeningDto.getRoomName(), "The screening's room cannot be null");
        Objects.requireNonNull(screeningDto.getStartingTime(), "The screening's starting time cannot be null");
        Optional<MovieDto> movie = movieService.getMovieByTitle(screeningDto.getMovieTitle());
        if (movie.isEmpty()) {
            return "No movie can be found with the specified title";
        }
        int runtime = movieService.getMovieByTitle(screeningDto.getMovieTitle()).get().getRuntime();
        Optional<RoomDto> room = roomService.getRoomByName(screeningDto.getRoomName());
        if (room.isEmpty()) {
            return "No room can be found with the specified name";
        }

        for (ScreeningDto screening : getScreeningsInSpecifiedRoom(screeningDto.getRoomName())) {
            int oldRuntime = movieService.getMovieByTitle(screening.getMovieTitle()).get().getRuntime();
            try {
                if (isOverLapping(screening.getStartingTime(), oldRuntime, screeningDto.getStartingTime(), runtime)) {
                    return "There is an overlapping screening";
                } else if (isOverLapping(screening.getStartingTime(),
                        oldRuntime + 10, screeningDto.getStartingTime(), runtime)) {
                    return "This would start in the break period after another screening in this room";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Screening screening = new Screening(screeningDto.getMovieTitle(),
                screeningDto.getRoomName(), screeningDto.getStartingTime());
        screeningRepository.save(screening);
        return "Screening successfully created!";
    }

    @Override
    public void deleteScreening(ScreeningDto screening) {
        Screening.ScreeningKey screeningKey = new Screening.ScreeningKey(screening.getMovieTitle(),
                screening.getRoomName(), screening.getStartingTime());
        screeningRepository.deleteById(screeningKey);
    }

    @Override
    public List<ScreeningDto> listScreenings() {
        return screeningRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> getScreeningsInSpecifiedRoom(String roomName) {
        return screeningRepository.findAllByRoomName(roomName).stream()
                .map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<ScreeningDto> getScreeningByKey(Screening.ScreeningKey screeningKey) {
        return convertEntityToDto(screeningRepository.findById(screeningKey));
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .movieTitle(screening.getMovieTitle())
                .roomName(screening.getRoomName())
                .startingTime(screening.getStaringTime())
                .build();
    }

    private Optional<ScreeningDto> convertEntityToDto(Optional<Screening> screening) {
        return screening.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(screening.get()));
    }


    private boolean isOverLapping(String originalStartDate, int originalRuntime, String newStartDate, int newRuntime)
            throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date originalStart = format.parse(originalStartDate);
        Date originalEnd = DateUtils.addMinutes(originalStart, originalRuntime);
        Date newStart = format.parse(newStartDate);
        Date newEnd = DateUtils.addMinutes(newStart, newRuntime);
        return originalStart.before(newEnd) && newStart.before(originalEnd);

    }
}
