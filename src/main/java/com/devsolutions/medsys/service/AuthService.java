package com.devsolutions.medsys.service;

import com.devsolutions.medsys.config.security.JwtUtil;
import com.devsolutions.medsys.dto.auth.AtendenteRegisterRequestDTO;
import com.devsolutions.medsys.dto.auth.DoctorRegisterRequestDTO;
import com.devsolutions.medsys.dto.auth.LoginRequestDTO;
import com.devsolutions.medsys.dto.auth.LoginResponseDTO;
import com.devsolutions.medsys.dto.auth.PatientRegisterRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.user.UserResponseDTO;
import com.devsolutions.medsys.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PatientService patientService;
    private final DoctorService doctorService;

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

    @Transactional
    public PatientResponseDTO registerPatient(PatientRegisterRequestDTO dto) {
        User user = userService.createUser(dto.email(), dto.password(), "PATIENT");
        return patientService.createFromUser(user, dto);
    }

    @Transactional
    public DoctorResponseDTO registerDoctor(DoctorRegisterRequestDTO dto) {
        User user = userService.createUser(dto.email(), dto.password(), "DOCTOR");
        return doctorService.createFromUser(user, dto);
    }

    @Transactional
    public UserResponseDTO registerAtendente(AtendenteRegisterRequestDTO dto) {
        User user = userService.createUser(dto.email(), dto.password(), "ATENDENTE");
        return new UserResponseDTO(user.getId(), user.getEmail(), user.isActive(), user.getCreatedAt());
    }
}