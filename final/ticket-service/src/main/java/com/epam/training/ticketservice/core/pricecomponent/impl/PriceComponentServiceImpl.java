package com.epam.training.ticketservice.core.pricecomponent.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PriceComponentServiceImpl implements PriceComponentService {

    private final PriceComponentRepository priceComponentRepository;
    private final MovieService movieService;
    private final RoomService roomService;
    private final ScreeningService screeningService;

    @Autowired
    public PriceComponentServiceImpl(PriceComponentRepository priceComponentRepository, MovieService movieService,
                                     RoomService roomService, ScreeningService screeningService) {
        this.priceComponentRepository = priceComponentRepository;
        this.movieService = movieService;
        this.roomService = roomService;
        this.screeningService = screeningService;
    }

    @Override
    public void createPriceComponent(PriceComponentDto priceComponentDto) {
        Objects.requireNonNull(priceComponentDto, "Price component cannot be null");
        Objects.requireNonNull(priceComponentDto.getName(), "The price component's name cannot be null");

        PriceComponent priceComponent = new PriceComponent(priceComponentDto.getName(), priceComponentDto.getValue());
        priceComponentRepository.save(priceComponent);

    }

    @Override
    public String attachPriceComponentToRoom(String name, String roomName) {
        Optional<PriceComponent> priceComponent = priceComponentRepository.findById(name);
        if (priceComponent.isEmpty()) {
            return "No price component exists with such name";
        }
        return roomService.updateChangeInPrice(roomName, priceComponent.get().getValue());
    }

    @Override
    public String attachPriceComponentToMovie(String name, String movieTitle) {
        Optional<PriceComponent> priceComponent = priceComponentRepository.findById(name);
        if (priceComponent.isEmpty()) {
            return "No price component exists with such name";
        }
        return movieService.updateChangeInPrice(movieTitle, priceComponent.get().getValue());
    }

    @Override
    public String attachPriceComponentToScreening(String name, String movieTitle, String roomName,
                                                  String startingTime) {
        Optional<PriceComponent> priceComponent = priceComponentRepository.findById(name);
        if (priceComponent.isEmpty()) {
            return "No price component exists with such name";
        }
        return screeningService.updateChangeInPrice(movieTitle, roomName, startingTime,
                priceComponent.get().getValue());
    }

    public int getChangeInPriceForMovie(String movieTitle) {
        MovieDto movieDto = movieService.getMovieByTitle(movieTitle).orElseThrow(() -> new IllegalStateException("Movie cannot be found"));
        return movieDto.getChangeInPrice();
    }

    public int getChangeInPriceForRoom(String roomName) {
        RoomDto roomDto = roomService.getRoomByName(roomName).orElseThrow(() -> new IllegalStateException("Room cannot be found"));
        return roomDto.getChangeInPrice();
    }

    public int getChangeInPriceForScreening(String movieTitle, String roomName, String startingTime) {
        Screening.ScreeningKey screeningKey = new Screening.ScreeningKey(movieTitle, roomName, startingTime);
        ScreeningDto screeningDto = screeningService.getScreeningByKey(screeningKey).orElseThrow(() -> new IllegalStateException("Screening cannot be found"));
        return screeningDto.getChangeInPrice();
    }
}
