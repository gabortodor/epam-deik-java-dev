package com.epam.training.ticketservice.core.booking;


import com.epam.training.ticketservice.core.booking.model.BookingDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    String createBooking(BookingDto bookingDto);

    List<BookingDto> listBookingForUser(String username);

    Optional<Integer> getPriceForBooking(BookingDto bookingDto);

    void setBasePrice(int newBasePrice);
}
