package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;

import java.util.List;
import java.util.Optional;

public interface ScreeningService {

    String createScreening(ScreeningDto screeningDto);

    void deleteScreening(ScreeningDto screeningDto);

    List<ScreeningDto> listScreenings();

    List<ScreeningDto> getScreeningsInSpecifiedRoom(String roomName);

    Optional<ScreeningDto> getScreeningByKey(Screening.ScreeningKey screeningKey);
}
