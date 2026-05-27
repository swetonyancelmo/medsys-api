package com.devsolutions.medsys.dto.clinic;

public record ClinicUpdateDTO(
        String name,
        String phone,
        String address,
        String email
) {
}
