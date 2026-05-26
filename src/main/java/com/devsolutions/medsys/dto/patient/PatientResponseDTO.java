package com.devsolutions.medsys.dto.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PatientResponseDTO(
        UUID id,
        UUID userId,
        String email,
        String name,
        String cpf,
        String phone,
        LocalDate birthDate,
        String address,
        Boolean active,
        LocalDateTime createdAt
) {
}
