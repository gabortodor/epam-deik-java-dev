package com.epam.training.ticketservice.core.user.model;

import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import lombok.Data;

@Data
public class UserDto {

    private final String username;
    private final Role role;

}
