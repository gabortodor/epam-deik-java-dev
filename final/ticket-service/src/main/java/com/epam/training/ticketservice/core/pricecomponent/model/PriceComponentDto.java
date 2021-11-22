package com.epam.training.ticketservice.core.pricecomponent.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PriceComponentDto {

    private final String name;

    private final int value;
}
