package com.devsolutions.medsys.dto.auth;

import java.util.List;

public record LoginResponseDTO(
        String token,
        String email,
        List<String> roles
) {}
