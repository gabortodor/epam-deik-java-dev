package com.epam.training.ticketservice.core.booking.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingDto {
    private final String movieTitle;
    private final String roomName;
    private final String startingTime;
    private final String seats;
    private final String username;
    private final int price;


    @Override
    public String toString() {
        return "Seats ("
                +  String.join("), (", List.of(seats.split(" ")))
                + ") on " + movieTitle + " in room " + roomName + " starting at "
                + startingTime + " for " + price + " HUF";


    }
}
