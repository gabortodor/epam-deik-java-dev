package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Screening.ScreeningKey> {

    @Query("SELECT s FROM Screening s WHERE s.screeningKey.roomName = ?1")
    List<Screening> findAllByRoomName(String roomName);
}
