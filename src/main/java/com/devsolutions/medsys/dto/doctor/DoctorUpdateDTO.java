package com.devsolutions.medsys.dto.doctor;

import java.util.UUID;

public record DoctorUpdateDTO(
        String name,
        String phone,
        UUID specialtyId,
        Integer appointmentDurationMin
) {
}
