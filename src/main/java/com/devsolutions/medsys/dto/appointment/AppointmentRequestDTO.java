package com.devsolutions.medsys.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDTO(
        @NotNull(message = "O ID do médico é obrigatório")
        UUID doctorId,

        @NotNull(message = "O ID do paciente é obrigatório")
        UUID patientId,

        @NotNull(message = "A data do agendamento é obrigatória")
        @Future(message = "O agendamento deve ser em uma data futura")
        LocalDateTime scheduledAt,

        @Size(max = 500, message = "O motivo não pode exceder 500 caracteres")
        String reason,

        String notes
) {
}
