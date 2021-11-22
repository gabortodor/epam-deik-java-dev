package com.epam.training.ticketservice.core.room.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomDto {

    private final String name;

    private final Integer rowCount;

    private final Integer columnCount;

    private final Integer changeInPrice;

    @Override
    public String toString() {
        return "Room " + name
                + " with " + (rowCount * columnCount)
                + " seats, " + rowCount + " rows and "
                + columnCount + " columns";
    }
}
