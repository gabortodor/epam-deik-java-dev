package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
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

    private final PriceComponentService priceComponentService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, RoomService roomService, ScreeningService screeningService,
                              PriceComponentService priceComponentService) {
        this.bookingRepository = bookingRepository;
        this.screeningService = screeningService;
        this.roomService = roomService;
        this.priceComponentService = priceComponentService;
    }

    @Override
    public String createBooking(BookingDto bookingDto) {
        if (!isValidBooking(bookingDto)) {
            return "Cannot find specified screening";
        }
        Optional<String> invalidSeat = checkValidSeats(bookingDto);
        if (invalidSeat.isPresent()) {
            return "Seat (" + invalidSeat.get() + ") does not exists in this room";
        }

        Optional<String> bookedSeat = checkSeatsBooked(bookingDto);
        if (bookedSeat.isPresent()) {
            return "Seat (" + bookedSeat.get() + ") is already taken";
        }
        int price = getPriceForBooking(bookingDto).orElseThrow(() -> new IllegalStateException("Invalid booking"));

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
            Optional<String> overlapping = wantedSeats.stream().filter(bookedSeats::contains).findFirst();
            if (overlapping.isPresent())
                return overlapping;
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
        Objects.requireNonNull(bookingDto, "Screening cannot be null");
        Objects.requireNonNull(bookingDto.getMovieTitle(), "The booking's movie cannot be null");
        Objects.requireNonNull(bookingDto.getRoomName(), "The booking's room cannot be null");
        Objects.requireNonNull(bookingDto.getStartingTime(), "The booking's starting time cannot be null");
        Objects.requireNonNull(bookingDto.getSeats(), "The booked seats time cannot be null");

        Screening.ScreeningKey screeningKey = new Screening.ScreeningKey(bookingDto.getMovieTitle(),
                bookingDto.getRoomName(), bookingDto.getStartingTime());
        return screeningService.getScreeningByKey(screeningKey).isPresent();
    }

    public Optional<Integer> getPriceForBooking(BookingDto bookingDto) {
        if (!isValidBooking(bookingDto)) {
            return Optional.empty();
        }
        int moviePriceChange = priceComponentService.getChangeInPriceForMovie(bookingDto.getMovieTitle());
        int roomPriceChange = priceComponentService.getChangeInPriceForRoom(bookingDto.getRoomName());
        int screeningPriceChange = priceComponentService.getChangeInPriceForScreening(bookingDto.getMovieTitle(),
                bookingDto.getRoomName(), bookingDto.getStartingTime());

        int numberOfSeats = bookingDto.getSeats().split(" ").length;

        return Optional.of(numberOfSeats * (basePrice + moviePriceChange + roomPriceChange + screeningPriceChange));
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
