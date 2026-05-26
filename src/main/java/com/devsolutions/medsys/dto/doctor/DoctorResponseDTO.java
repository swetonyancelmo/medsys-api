package com.devsolutions.medsys.dto.doctor;

import java.time.LocalDateTime;
import java.util.UUID;

public record DoctorResponseDTO(
        UUID id,
        UUID userId,
        String email,
        UUID specialtyId,
        String specialtyName,
        String name,
        String crm,
        String phone,
        Integer appointmentDurationMin,
        Boolean active,
        LocalDateTime createdAt
) {
}
