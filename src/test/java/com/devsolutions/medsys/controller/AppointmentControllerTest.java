package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.dto.appointment.AppointmentRequestDTO;
import com.devsolutions.medsys.dto.appointment.AppointmentResponseDTO;
import com.devsolutions.medsys.enums.AppointmentStatus;
import com.devsolutions.medsys.service.AppointmentService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ATENDENTE", "DOCTOR", "PATIENT"})
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private AppointmentService service;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CacheManager cacheManager;

    private static final UUID FIXED_ID   = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID DOCTOR_ID  = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID PATIENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");

    private AppointmentResponseDTO buildResponse() {
        return new AppointmentResponseDTO(
                FIXED_ID, DOCTOR_ID, "Dr. João", PATIENT_ID, "Maria",
                LocalDateTime.now().plusDays(1), AppointmentStatus.SCHEDULED,
                "Consulta de rotina", null, LocalDateTime.now()
        );
    }

    @Test
    void deveAgendarConsultaComSucesso() throws Exception {
        AppointmentRequestDTO request = new AppointmentRequestDTO(
                DOCTOR_ID, PATIENT_ID, LocalDateTime.now().plusDays(1), "Consulta de rotina", null
        );
        when(service.schedule(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void deveBuscarConsultaPorId() throws Exception {
        when(service.findById(FIXED_ID)).thenReturn(buildResponse());

        mockMvc.perform(get("/appointments/{id}", FIXED_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveBuscarConsultasPorMedicoEPeriodo() throws Exception {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        when(service.findByDoctorAndDateRange(eq(DOCTOR_ID), any(), any()))
                .thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/appointments/doctor/{id}", DOCTOR_ID)
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveBuscarConsultasPorPaciente() throws Exception {
        when(service.findByPatient(PATIENT_ID)).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/appointments/findByPatient/{id}", PATIENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveBuscarConsultasPorPeriodo() throws Exception {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        when(service.findByDateRange(any(), any())).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/appointments/range")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveCancelarConsulta() throws Exception {
        AppointmentResponseDTO response = new AppointmentResponseDTO(
                FIXED_ID, DOCTOR_ID, "Dr. João", PATIENT_ID, "Maria",
                LocalDateTime.now().plusDays(1), AppointmentStatus.CANCELLED,
                null, null, LocalDateTime.now()
        );
        when(service.cancel(FIXED_ID)).thenReturn(response);

        mockMvc.perform(patch("/appointments/{id}/cancel", FIXED_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}