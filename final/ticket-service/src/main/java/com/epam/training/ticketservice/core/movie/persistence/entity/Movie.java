package com.epam.training.ticketservice.core.movie.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Movie {

    @Id
    private String title;

    private String genre;

    private Integer runtime;

    private int changeInPrice = 0;

    public Movie(String title, String genre, Integer runtime) {
        this.title = title;
        this.genre = genre;
        this.runtime = runtime;
    }
}
