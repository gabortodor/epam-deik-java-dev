package com.epam.training.ticketservice.core.pricecomponent.persistence.repository;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceComponentRepository extends JpaRepository<PriceComponent, String> {
}
