package com.devsolutions.medsys.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtendenteRegisterRequestDTO(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
) {}