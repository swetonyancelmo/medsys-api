package com.devsolutions.medsys.dto.appointment;

import com.devsolutions.medsys.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        UUID doctorId,
        String doctorName,
        UUID patientId,
        String patientName,
        LocalDateTime scheduledAt,
        AppointmentStatus status,
        String reason,
        String notes,
        LocalDateTime createdAt
) {
}
