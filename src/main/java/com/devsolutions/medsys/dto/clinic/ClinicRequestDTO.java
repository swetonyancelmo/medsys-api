package com.devsolutions.medsys.dto.clinic;

import jakarta.validation.constraints.NotBlank;

public record ClinicRequestDTO(
        @NotBlank String name,
        @NotBlank String cnpj,
        String phone,
        String address,
        String email
) {
}
