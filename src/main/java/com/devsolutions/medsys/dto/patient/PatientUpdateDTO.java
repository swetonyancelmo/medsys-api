package com.devsolutions.medsys.dto.patient;

import java.time.LocalDate;

public record PatientUpdateDTO(
        String name,
        String phone,
        LocalDate birthDate,
        String address
) {
}
