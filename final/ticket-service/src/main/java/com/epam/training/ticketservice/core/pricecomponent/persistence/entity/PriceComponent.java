package com.epam.training.ticketservice.core.pricecomponent.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PriceComponent {

    @Id
    String name;
    int value;
}
