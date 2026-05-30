package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtAuthFilter;
import com.devsolutions.medsys.config.security.JwtUtil;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private PatientService patientService;

    // O @WebMvcTest sobe o SecurityConfig, então JwtUtil e JwtAuthFilter
    // precisam ser mockados também (dependências do filtro JWT)
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void findById_deveRetornar200QuandoEncontrado() {
        UUID id = UUID.randomUUID();
        PatientResponseDTO response = new PatientResponseDTO(id, UUID.randomUUID(), "maria@gmail.com",
                "Maria Lima", "12344455577", "81988552233", LocalDate.of(1990, 5, 20),
                "Rua 2", true, LocalDateTime.now(), LocalDateTime.now());

        given(patientService.findById(id)).willReturn(response);

        assertThat(mvc.get().uri("/patients/" + id))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.name")
                .isEqualTo("Maria Silva");
    }

    @Test
    @WithMockUser(roles = "PATIENT") // PATIENT não tem acesso ao GET /
    void findAll_deveRetornar403ParaPaciente() {
        assertThat(mvc.get().uri("/patients"))
                .hasStatus(403);
    }

    @Test
    void findById_semToken_deveRetornar401() {
        assertThat(mvc.get().uri("/patients/" + UUID.randomUUID()))
                .hasStatus(401);
    }
}