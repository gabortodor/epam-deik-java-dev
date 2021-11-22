package com.epam.training.ticketservice.core.booking.persistence.repository;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByMovieTitleAndRoomNameAndStartingTime(String movieTitle,
                                                                String roomName,
                                                                String startingTime);

    List<Booking> findAllByUsername(String username);
}
