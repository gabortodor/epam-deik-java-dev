package com.epam.training.ticketservice.core.pricecomponent;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.pricecomponent.impl.PriceComponentServiceImpl;
import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class PriceComponentServiceImplTest {

    private final PriceComponentRepository priceComponentRepository = mock(PriceComponentRepository.class);

    private final RoomService roomService = mock(RoomServiceImpl.class);
    private final MovieService movieService = mock(MovieServiceImpl.class);
    private final ScreeningService screeningService = mock(ScreeningServiceImpl.class);

    private final PriceComponentService underTest = new PriceComponentServiceImpl(priceComponentRepository, movieService, roomService, screeningService);


    @Test
    public void testCreatePriceComponentShouldThrowNullPointerExceptionWhenPriceComponentIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createPriceComponent(null));
    }

    @Test
    public void testCreatePriceComponentShouldThrowNullPointerExceptionWhenPriceComponentNameIsNull() {
        // Given
        PriceComponentDto priceComponentDto = PriceComponentDto.builder()
                .name(null)
                .value(0)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createPriceComponent(priceComponentDto));
    }
}
