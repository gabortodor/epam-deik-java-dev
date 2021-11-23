package com.epam.training.ticketservice.core.pricecomponent;

import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;

public interface PriceComponentService {

    void createPriceComponent(PriceComponentDto priceComponentDto);

    String attachPriceComponentToRoom(String name, String roomName);

    String attachPriceComponentToMovie(String name, String movieTitle);

    String attachPriceComponentToScreening(String name, String movieTitle, String roomName, String startingTime);

    int getChangeInPriceForMovie(String movieTitle);

    int getChangeInPriceForRoom(String roomName);

    int getChangeInPriceForScreening(String movieTitle, String roomName, String startingTime);
}
