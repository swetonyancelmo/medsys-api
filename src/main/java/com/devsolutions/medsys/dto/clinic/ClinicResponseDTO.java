package com.devsolutions.medsys.dto.clinic;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClinicResponseDTO(
        UUID id,
        String name,
        String cnpj,
        String phone,
        String address,
        String email,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
