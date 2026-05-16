package com.devsolutions.medsys.dto.doctorAvailability;

import java.time.LocalTime;
import java.util.UUID;

public record DoctorAvailabilityResponseDTO(
        UUID id,
        UUID doctorId,
        Integer dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        boolean active
) {
}
