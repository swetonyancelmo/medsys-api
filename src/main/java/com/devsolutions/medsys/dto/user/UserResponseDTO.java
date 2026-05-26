package com.devsolutions.medsys.dto.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String email,
        boolean active,
        LocalDateTime createdAt
) {}