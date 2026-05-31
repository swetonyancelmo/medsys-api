package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.dto.auth.*;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.user.UserResponseDTO;
import com.devsolutions.medsys.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ATENDENTE", "DOCTOR", "PATIENT"})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CacheManager cacheManager;

    @Test
    void deveRealizarLoginComSucesso() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("user@medsys.com", "Senha@123");
        LoginResponseDTO response = new LoginResponseDTO("jwt-token", "user@medsys.com", List.of("ROLE_ATENDENTE"));
        when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("user@medsys.com"));
    }

    @Test
    void deveRegistrarPacienteComSucesso() throws Exception {
        PatientRegisterRequestDTO request = new PatientRegisterRequestDTO(
                "paciente@medsys.com", "Senha@123!", "João Silva",
                "123.456.789-00", "11999999999", LocalDate.of(1990, 1, 1), "Rua A, 123"
        );
        PatientResponseDTO response = new PatientResponseDTO(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.randomUUID(), "paciente@medsys.com", "João Silva",
                "123.456.789-00", "11999999999", LocalDate.of(1990, 1, 1),
                "Rua A, 123", true, LocalDateTime.now(), LocalDateTime.now()
        );
        when(authService.registerPatient(any())).thenReturn(response);

        mockMvc.perform(post("/auth/register/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("paciente@medsys.com"));
    }

    @Test
    void deveRegistrarMedicoComSucesso() throws Exception {
        UUID specialtyId = UUID.randomUUID();
        DoctorRegisterRequestDTO request = new DoctorRegisterRequestDTO(
                "medico@medsys.com", "Senha@123!", "Dr. João", "CRM-12345",
                specialtyId, "11999999999", 30
        );
        DoctorResponseDTO response = new DoctorResponseDTO(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.randomUUID(), "medico@medsys.com", specialtyId, "Cardiologia",
                "Dr. João", "CRM-12345", "11999999999", 30, true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        when(authService.registerDoctor(any())).thenReturn(response);

        mockMvc.perform(post("/auth/register/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("medico@medsys.com"));
    }

    @Test
    void deveRegistrarAtendenteComSucesso() throws Exception {
        AtendenteRegisterRequestDTO request = new AtendenteRegisterRequestDTO(
                "atendente@medsys.com", "Senha@123!"
        );
        UserResponseDTO response = new UserResponseDTO(
                UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "atendente@medsys.com", true, LocalDateTime.now()
        );
        when(authService.registerAtendente(any())).thenReturn(response);

        mockMvc.perform(post("/auth/register/atendente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("atendente@medsys.com"));
    }
}
