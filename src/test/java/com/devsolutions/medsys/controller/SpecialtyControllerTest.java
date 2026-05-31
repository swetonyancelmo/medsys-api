package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyRequestDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyResponseDTO;
import com.devsolutions.medsys.service.DoctorService;
import com.devsolutions.medsys.service.SpecialtyService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpecialtyController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ATENDENTE", "DOCTOR", "PATIENT"})
class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private SpecialtyService specialtyService;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CacheManager cacheManager;

    private static final UUID FIXED_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private SpecialtyResponseDTO buildSpecialtyResponse() {
        return new SpecialtyResponseDTO(FIXED_ID, "Cardiologia", "Doenças do coração");
    }

    private DoctorResponseDTO buildDoctorResponse() {
        return new DoctorResponseDTO(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.randomUUID(), "medico@medsys.com", FIXED_ID, "Cardiologia",
                "Dr. João", "CRM-12345", "11999999999", 30, true,
                LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void deveCriarEspecialidadeComSucesso() throws Exception {
        SpecialtyRequestDTO request = new SpecialtyRequestDTO("Cardiologia", "Doenças do coração");
        when(specialtyService.create(any())).thenReturn(buildSpecialtyResponse());

        mockMvc.perform(post("/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()))
                .andExpect(jsonPath("$.name").value("Cardiologia"));
    }

    @Test
    void deveListarEspecialidades() throws Exception {
        when(specialtyService.findAll(any())).thenReturn(List.of(buildSpecialtyResponse()));

        mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveBuscarEspecialidadePorId() throws Exception {
        when(specialtyService.findById(FIXED_ID)).thenReturn(buildSpecialtyResponse());

        mockMvc.perform(get("/specialties/{id}", FIXED_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveBuscarMedicosPorEspecialidade() throws Exception {
        when(specialtyService.findById(FIXED_ID)).thenReturn(buildSpecialtyResponse());
        when(doctorService.findBySpecialty(FIXED_ID)).thenReturn(List.of(buildDoctorResponse()));

        mockMvc.perform(get("/specialties/{id}/doctors", FIXED_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
