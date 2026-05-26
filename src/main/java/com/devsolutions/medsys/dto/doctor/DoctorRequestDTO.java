package com.devsolutions.medsys.dto.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DoctorRequestDTO(
        @NotNull UUID userId,
        @NotNull UUID specialtyId,
        @NotBlank String name,
        @NotBlank String crm,
        String phone,
        Integer appointmentDurationMin
) {
}
