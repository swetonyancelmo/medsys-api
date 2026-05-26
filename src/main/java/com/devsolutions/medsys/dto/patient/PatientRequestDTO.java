package com.devsolutions.medsys.dto.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PatientRequestDTO(
        @NotNull UUID userId,
        @NotBlank String name,
        @NotBlank String cpf,
        String phone,
        LocalDate birthDate,
        String address
) {
}
