package com.devsolutions.medsys.dto.prescription;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PrescriptionRequestDTO(
        @NotNull(message = "O ID da consulta é obrigatório")
        UUID appointmentId,
        @NotNull(message = "A descrição é obrigatório")
        String description,
        String medications,
        LocalDate expiresAt
) {
}
