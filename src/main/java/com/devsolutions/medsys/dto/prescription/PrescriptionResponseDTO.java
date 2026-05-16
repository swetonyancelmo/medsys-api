package com.devsolutions.medsys.dto.prescription;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PrescriptionResponseDTO(
        UUID id,
        UUID appointmentId,
        String description,
        String medications,
        LocalDate expiresAt,
        LocalDateTime createdAt
) {
}
