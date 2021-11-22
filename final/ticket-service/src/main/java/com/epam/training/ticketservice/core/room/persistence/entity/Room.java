package com.epam.training.ticketservice.core.room.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Room {
    @Id
    private String name;

    private Integer rowCount;

    private Integer columnCount;

    private int changeInPrice = 0;

    public Room(String name, Integer rowCount, Integer columnCount) {
        this.name = name;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }
}
