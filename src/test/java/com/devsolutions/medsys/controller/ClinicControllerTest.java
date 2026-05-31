package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.dto.clinic.ClinicRequestDTO;
import com.devsolutions.medsys.dto.clinic.ClinicResponseDTO;
import com.devsolutions.medsys.dto.clinic.ClinicUpdateDTO;
import com.devsolutions.medsys.service.ClinicService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClinicController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ATENDENTE", "DOCTOR", "PATIENT"})
class ClinicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private ClinicService service;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CacheManager cacheManager;

    private static final UUID FIXED_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private ClinicResponseDTO buildResponse() {
        return new ClinicResponseDTO(
                FIXED_ID, "Clínica Teste", "11.222.333/0001-44",
                "11999999999", "Rua A, 123", "clinica@email.com",
                true, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void deveCriarClinicaComSucesso() throws Exception {
        ClinicRequestDTO request = new ClinicRequestDTO(
                "Clínica Teste", "11.222.333/0001-44", "11999999999", "Rua A, 123", "clinica@email.com"
        );
        when(service.create(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/clinics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()))
                .andExpect(jsonPath("$.name").value("Clínica Teste"));
    }

    @Test
    void deveListarClinicasComOrdemAscendente() throws Exception {
        when(service.findAll(any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildResponse())));

        mockMvc.perform(get("/clinics").param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveListarClinicasComOrdemDescendente() throws Exception {
        when(service.findAll(any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildResponse())));

        mockMvc.perform(get("/clinics").param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarClinicaPorId() throws Exception {
        when(service.findById(FIXED_ID)).thenReturn(buildResponse());

        mockMvc.perform(get("/clinics/{id}", FIXED_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveAtualizarClinica() throws Exception {
        ClinicUpdateDTO dto = new ClinicUpdateDTO("Novo Nome", "11888888888", "Nova Rua, 456", "novo@email.com");
        when(service.update(eq(FIXED_ID), any())).thenReturn(buildResponse());

        mockMvc.perform(patch("/clinics/{id}", FIXED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveDeletarClinica() throws Exception {
        doNothing().when(service).delete(FIXED_ID);

        mockMvc.perform(delete("/clinics/{id}", FIXED_ID))
                .andExpect(status().isNoContent());
    }
}
