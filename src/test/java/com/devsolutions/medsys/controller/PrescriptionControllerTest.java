package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.dto.prescription.PrescriptionRequestDTO;
import com.devsolutions.medsys.dto.prescription.PrescriptionResponseDTO;
import com.devsolutions.medsys.service.PrescriptionService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrescriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ATENDENTE", "DOCTOR", "PATIENT"})
class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private PrescriptionService service;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CacheManager cacheManager;

    private static final UUID FIXED_ID      = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID APPOINTMENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private PrescriptionResponseDTO buildResponse() {
        return new PrescriptionResponseDTO(
                FIXED_ID, APPOINTMENT_ID, "Tomar bastante água",
                "Dipirona 500mg 1x ao dia", LocalDate.now().plusDays(30), LocalDateTime.now()
        );
    }

    @Test
    void deveCriarReceitaComSucesso() throws Exception {
        PrescriptionRequestDTO request = new PrescriptionRequestDTO(
                APPOINTMENT_ID, "Tomar bastante água", "Dipirona 500mg 1x ao dia",
                LocalDate.now().plusDays(30)
        );
        when(service.createPrescription(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()))
                .andExpect(jsonPath("$.appointmentId").value(APPOINTMENT_ID.toString()));
    }

    @Test
    void deveBuscarReceitaPorId() throws Exception {
        when(service.findById(FIXED_ID)).thenReturn(buildResponse());

        mockMvc.perform(get("/prescriptions/{id}", FIXED_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveBuscarReceitaPorConsulta() throws Exception {
        when(service.findByAppointmentId(APPOINTMENT_ID)).thenReturn(buildResponse());

        mockMvc.perform(get("/prescriptions/appointment/{appointmentId}", APPOINTMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentId").value(APPOINTMENT_ID.toString()));
    }
}
