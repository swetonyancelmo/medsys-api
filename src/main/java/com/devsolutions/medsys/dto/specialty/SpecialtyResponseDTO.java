package com.devsolutions.medsys.dto.specialty;

import java.util.UUID;

public record SpecialtyResponseDTO(
        UUID id,
        String name,
        String description
) {
}
