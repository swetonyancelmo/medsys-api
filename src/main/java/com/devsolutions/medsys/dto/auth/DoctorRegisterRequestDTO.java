package com.devsolutions.medsys.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DoctorRegisterRequestDTO(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String name,
        @NotBlank String crm,
        @NotNull UUID specialtyId,
        String phone,
        Integer appointmentDurationMin
) {}
