package com.epam.training.ticketservice.core.movie.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MovieDto {

    private final String title;

    private final String genre;

    private final Integer runtime;

    @Override
    public String toString() {
        return title + " (" + genre + ", " + runtime + " minutes)";
    }
}
