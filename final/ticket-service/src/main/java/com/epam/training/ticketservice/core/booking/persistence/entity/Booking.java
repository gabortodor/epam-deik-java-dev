package com.epam.training.ticketservice.core.booking.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String movieTitle;
    private String roomName;
    private String startingTime;
    private String seats;
    private String username;
    private int price;

    public Booking(String movieTitle, String roomName, String startingTime, String seats, String username, int price) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startingTime = startingTime;
        this.seats = seats;
        this.username = username;
        this.price = price;
    }
}
