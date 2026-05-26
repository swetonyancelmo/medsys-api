package com.devsolutions.medsys.dto.prescription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PrescriptionRequestDTO(
        @NotNull(message = "O ID da consulta é obrigatório")
        UUID appointmentId,
        @NotBlank(message = "A descrição é obrigatória")
        String description,
        @NotBlank(message = "Os medicamentos são obrigatórios")
        String medications,
        LocalDate expiresAt
) {
}
