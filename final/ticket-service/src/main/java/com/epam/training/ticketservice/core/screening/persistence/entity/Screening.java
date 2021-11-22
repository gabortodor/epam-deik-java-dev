package com.epam.training.ticketservice.core.screening.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
public class Screening {

    @EmbeddedId
    private ScreeningKey screeningKey;

    private int changeInPrice = 0;

    public Screening(String movieTitle, String roomName, String startingTime) {
        this.screeningKey = new ScreeningKey(movieTitle, roomName, startingTime);
    }

    public String getMovieTitle() {
        return this.screeningKey.getMovieTitle();
    }

    public String getRoomName() {
        return this.screeningKey.getRoomName();
    }

    public String getStaringTime() {
        return this.screeningKey.getStartingTime();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ScreeningKey implements Serializable {

        private String movieTitle;

        private String roomName;

        private String startingTime;
    }
}
