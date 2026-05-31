package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.dto.patient.PatientRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientUpdateDTO;
import com.devsolutions.medsys.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ATENDENTE", "DOCTOR", "PATIENT"})
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CacheManager cacheManager;

    private static final UUID FIXED_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID USER_ID   = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private PatientResponseDTO buildResponse() {
        return new PatientResponseDTO(
                FIXED_ID, USER_ID, "paciente@medsys.com", "João Silva",
                "123.456.789-00", "11999999999", LocalDate.of(1990, 1, 1),
                "Rua A, 123", true, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void deveCriarPacienteComSucesso() throws Exception {
        PatientRequestDTO request = new PatientRequestDTO(
                USER_ID, "João Silva", "123.456.789-00", "11999999999",
                LocalDate.of(1990, 1, 1), "Rua A, 123"
        );
        when(patientService.create(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()))
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    void deveListarPacientesComOrdemAscendente() throws Exception {
        when(patientService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildResponse())));

        mockMvc.perform(get("/patients").param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveListarPacientesComOrdemDescendente() throws Exception {
        when(patientService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildResponse())));

        mockMvc.perform(get("/patients").param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarPacientePorId() throws Exception {
        when(patientService.findById(FIXED_ID)).thenReturn(buildResponse());

        mockMvc.perform(get("/patients/{id}", FIXED_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveAtualizarPaciente() throws Exception {
        PatientUpdateDTO dto = new PatientUpdateDTO(
                "João Atualizado", "11888888888", LocalDate.of(1990, 1, 1), "Nova Rua, 456"
        );
        when(patientService.update(eq(FIXED_ID), any())).thenReturn(buildResponse());

        mockMvc.perform(patch("/patients/{id}", FIXED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveDeletarPaciente() throws Exception {
        doNothing().when(patientService).delete(FIXED_ID);

        mockMvc.perform(delete("/patients/{id}", FIXED_ID))
                .andExpect(status().isNoContent());
    }
}
