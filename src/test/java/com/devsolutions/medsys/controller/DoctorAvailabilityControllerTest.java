package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityRequestDTO;
import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityResponseDTO;
import com.devsolutions.medsys.service.DoctorAvailabilityService;
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

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorAvailabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ATENDENTE", "DOCTOR"})
class DoctorAvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private DoctorAvailabilityService service;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CacheManager cacheManager;

    private static final UUID FIXED_ID  = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID DOCTOR_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private DoctorAvailabilityResponseDTO buildResponse() {
        return new DoctorAvailabilityResponseDTO(
                FIXED_ID, DOCTOR_ID, 1, LocalTime.of(8, 0), LocalTime.of(18, 0), true
        );
    }

    @Test
    void deveRegistrarDisponibilidadeComSucesso() throws Exception {
        DoctorAvailabilityRequestDTO request = new DoctorAvailabilityRequestDTO(
                DOCTOR_ID, 1, LocalTime.of(8, 0), LocalTime.of(18, 0)
        );
        when(service.registerAvailability(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/doctor-availabilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void deveBuscarDisponibilidadesPorMedico() throws Exception {
        when(service.findActiveAvailabilitiesByDoctor(DOCTOR_ID))
                .thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/doctor-availabilities/doctor/{doctorId}", DOCTOR_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveDesativarDisponibilidade() throws Exception {
        doNothing().when(service).disableAvailability(FIXED_ID);

        mockMvc.perform(patch("/doctor-availabilities/{id}/disable", FIXED_ID))
                .andExpect(status().isNoContent());
    }
}
