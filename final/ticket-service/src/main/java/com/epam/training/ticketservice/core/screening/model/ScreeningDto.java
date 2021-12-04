package com.epam.training.ticketservice.core.screening.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScreeningDto {

    private String movieTitle;

    private String roomName;

    private String startingTime;

    private final Integer changeInPrice;

}
