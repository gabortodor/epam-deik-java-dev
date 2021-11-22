package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private int basePrice = 1500;

    private final BookingRepository bookingRepository;

    private final ScreeningService screeningService;

    private final RoomService roomService;

    private final MovieService movieService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ScreeningService screeningService,
                              RoomService roomService, MovieService movieService) {
        this.bookingRepository = bookingRepository;
        this.screeningService = screeningService;
        this.roomService = roomService;
        this.movieService = movieService;
    }

    @Override
    public String createBooking(BookingDto bookingDto) {
        Objects.requireNonNull(bookingDto, "Screening cannot be null");
        Objects.requireNonNull(bookingDto.getMovieTitle(), "The booking's movie cannot be null");
        Objects.requireNonNull(bookingDto.getRoomName(), "The booking's room cannot be null");
        Objects.requireNonNull(bookingDto.getStartingTime(), "The booking's starting time cannot be null");
        Objects.requireNonNull(bookingDto.getSeats(), "The booked seats time cannot be null");
        Screening.ScreeningKey screeningKey = new Screening.ScreeningKey(bookingDto.getMovieTitle(),
                bookingDto.getRoomName(), bookingDto.getStartingTime());
        Optional<ScreeningDto> screeningOpt = screeningService.getScreeningByKey(screeningKey);
        if (screeningOpt.isEmpty()) {
            return "No such screening exists";
        }
        if (checkValidSeats(bookingDto).isPresent()) {
            return "Seat (" + checkValidSeats(bookingDto).get() + ") does not exists in this room";
        }
        if (checkSeatsBooked(bookingDto).isPresent()) {
            return "Seat (" + checkSeatsBooked(bookingDto).get() + ") is already taken";
        }
        if (calculatePriceForBooking(bookingDto) == -1) {
            return "Invalid arguments given";
        }
        int price = calculatePriceForBooking(bookingDto);

        Booking booking = new Booking(bookingDto.getMovieTitle(), bookingDto.getRoomName(),
                bookingDto.getStartingTime(), bookingDto.getSeats(), bookingDto.getUsername(), price);

        bookingRepository.save(booking);
        return "Seats booked: (" + String.join("), (", booking.getSeats().split(" "))
                + "); the price for this booking is " + booking.getPrice() + " HUF";
    }

    @Override
    public List<BookingDto> listBookingForUser(String username) {
        return bookingRepository.findAllByUsername(username).stream()
                .map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private Optional<String> checkSeatsBooked(BookingDto bookingDto) {
        List<Booking> bookings =
                bookingRepository.findAllByMovieTitleAndRoomNameAndStartingTime(
                        bookingDto.getMovieTitle(), bookingDto.getRoomName(),
                        bookingDto.getStartingTime());

        List<String> wantedSeats = List.of(bookingDto.getSeats().split(" "));

        for (Booking booking : bookings) {
            List<String> bookedSeats = List.of(booking.getSeats().split(" "));
            return wantedSeats.stream().filter(bookedSeats::contains).findFirst();
        }
        return Optional.empty();
    }

    private Optional<String> checkValidSeats(BookingDto bookingDto) {
        List<String> wantedSeats = List.of(bookingDto.getSeats().split(" "));
        RoomDto room = roomService.getRoomByName(bookingDto.getRoomName()).get();

        return wantedSeats.stream().filter(b -> Character.getNumericValue(b.charAt(0)) < 0
                || Character.getNumericValue(b.charAt(0)) > room.getRowCount()
                || Character.getNumericValue(b.charAt(2)) < 0
                || Character.getNumericValue(b.charAt(2)) > room.getColumnCount()).findFirst();

    }


    private boolean isValidBooking(BookingDto bookingDto) {
        Screening.ScreeningKey screeningKey = new Screening.ScreeningKey(bookingDto.getMovieTitle(),
                bookingDto.getRoomName(), bookingDto.getStartingTime());
        return movieService.getMovieByTitle(bookingDto.getMovieTitle()).isPresent()
                && roomService.getRoomByName(bookingDto.getRoomName()).isPresent()
                && screeningService.getScreeningByKey(screeningKey).isPresent();
    }

    public int calculatePriceForBooking(BookingDto bookingDto) {
        if (!isValidBooking(bookingDto)) {
            return -1;
        }
        MovieDto movie = movieService.getMovieByTitle(bookingDto.getMovieTitle()).get();
        RoomDto room = roomService.getRoomByName(bookingDto.getRoomName()).get();
        Screening.ScreeningKey screeningKey = new Screening.ScreeningKey(bookingDto.getMovieTitle(),
                bookingDto.getRoomName(), bookingDto.getStartingTime());
        ScreeningDto screening = screeningService.getScreeningByKey(screeningKey).get();
        int numberOfSeats = bookingDto.getSeats().split(" ").length;

        return numberOfSeats
                * (basePrice + movie.getChangeInPrice() + room.getChangeInPrice() + screening.getChangeInPrice());
    }

    public void setBasePrice(int newBasePrice) {
        this.basePrice = newBasePrice;
    }

    private BookingDto convertEntityToDto(Booking booking) {
        return BookingDto.builder()
                .movieTitle(booking.getMovieTitle())
                .roomName(booking.getRoomName())
                .startingTime(booking.getStartingTime())
                .seats(booking.getSeats())
                .username(booking.getUsername())
                .price(booking.getPrice())
                .build();
    }

    private Optional<BookingDto> convertEntityToDto(Optional<Booking> booking) {
        return booking.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(booking.get()));
    }


}
