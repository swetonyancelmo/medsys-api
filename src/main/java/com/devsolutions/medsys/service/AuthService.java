package com.devsolutions.medsys.service;

import com.devsolutions.medsys.config.security.JwtUtil;
import com.devsolutions.medsys.dto.auth.LoginRequestDTO;
import com.devsolutions.medsys.dto.auth.LoginResponseDTO;
import com.devsolutions.medsys.dto.auth.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtUtil.generateToken(dto.email(), roles);
        return new LoginResponseDTO(token, dto.email(), roles);
    }

    public void register(RegisterRequestDTO dto) {
        userService.createUser(dto.email(), dto.password(), dto.role());
    }
}
