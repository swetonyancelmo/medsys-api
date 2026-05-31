package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.dto.doctor.DoctorRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.doctor.DoctorUpdateDTO;
import com.devsolutions.medsys.service.DoctorService;
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

@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ATENDENTE", "DOCTOR"})
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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

    private static final UUID FIXED_ID     = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID USER_ID      = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID SPECIALTY_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");

    private DoctorResponseDTO buildResponse() {
        return new DoctorResponseDTO(
                FIXED_ID, USER_ID, "medico@medsys.com", SPECIALTY_ID, "Cardiologia",
                "Dr. João", "CRM-12345", "11999999999", 30, true,
                LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void deveCriarMedicoComSucesso() throws Exception {
        DoctorRequestDTO request = new DoctorRequestDTO(
                USER_ID, SPECIALTY_ID, "Dr. João", "CRM-12345", "11999999999", 30
        );
        when(doctorService.create(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()))
                .andExpect(jsonPath("$.name").value("Dr. João"));
    }

    @Test
    void deveListarMedicosComOrdemAscendente() throws Exception {
        when(doctorService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildResponse())));

        mockMvc.perform(get("/doctors").param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveListarMedicosComOrdemDescendente() throws Exception {
        when(doctorService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildResponse())));

        mockMvc.perform(get("/doctors").param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarMedicoPorId() throws Exception {
        when(doctorService.findById(FIXED_ID)).thenReturn(buildResponse());

        mockMvc.perform(get("/doctors/{id}", FIXED_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveAtualizarMedico() throws Exception {
        DoctorUpdateDTO dto = new DoctorUpdateDTO("Dr. João Atualizado", "11888888888", SPECIALTY_ID, 45);
        when(doctorService.update(eq(FIXED_ID), any())).thenReturn(buildResponse());

        mockMvc.perform(patch("/doctors/{id}", FIXED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
    }

    @Test
    void deveDeletarMedico() throws Exception {
        doNothing().when(doctorService).delete(FIXED_ID);

        mockMvc.perform(delete("/doctors/{id}", FIXED_ID))
                .andExpect(status().isNoContent());
    }
}
