package com.epam.training.ticketservice.core.booking;


import com.epam.training.ticketservice.core.booking.model.BookingDto;

import java.util.List;

public interface BookingService {
    String createBooking(BookingDto bookingDto);

    List<BookingDto> listBookingForUser(String username);

    int calculatePriceForBooking(BookingDto bookingDto);

    void setBasePrice(int newBasePrice);
}
