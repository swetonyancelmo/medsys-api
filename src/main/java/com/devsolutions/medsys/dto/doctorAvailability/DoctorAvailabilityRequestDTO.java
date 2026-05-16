package com.devsolutions.medsys.dto.doctorAvailability;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record DoctorAvailabilityRequestDTO(
        @NotNull(message = "O ID do médico é obrigatório")
        UUID doctorId,

        @NotNull(message = "O dia da semana é obrigatório")
        @Min(value = 1, message = "O dia da semana deve ser no mínimo 1 (Segunda-feira)")
        @Max(value = 7, message = "O dia da semana deve ser no máximo 7 (Domingo)")
        Integer dayOfWeek,

        @NotNull(message = "O horário de início é obrigatório")
        LocalTime startTime,

        @NotNull(message = "O horário de término é obrigatório")
        LocalTime endTime
) {
}
