package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.controller.docs.AuthControllerDocs;
import com.devsolutions.medsys.dto.auth.AtendenteRegisterRequestDTO;
import com.devsolutions.medsys.dto.auth.DoctorRegisterRequestDTO;
import com.devsolutions.medsys.dto.auth.LoginRequestDTO;
import com.devsolutions.medsys.dto.auth.LoginResponseDTO;
import com.devsolutions.medsys.dto.auth.PatientRegisterRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.user.UserResponseDTO;
import com.devsolutions.medsys.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints de autenticação e registro de usuários")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(
            value = "/register/patient",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<PatientResponseDTO> registerPatient(@Valid @RequestBody PatientRegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerPatient(dto));
    }

    @PostMapping(
            value = "/register/doctor",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ATENDENTE')")
    @Override
    public ResponseEntity<DoctorResponseDTO> registerDoctor(@Valid @RequestBody DoctorRegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerDoctor(dto));
    }

    @PostMapping(
            value = "/register/atendente",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ATENDENTE')")
    @Override
    public ResponseEntity<UserResponseDTO> registerAtendente(@Valid @RequestBody AtendenteRegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerAtendente(dto));
    }
}