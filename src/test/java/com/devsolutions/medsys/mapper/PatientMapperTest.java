package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.model.Patient;
import com.devsolutions.medsys.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PatientMapperTest {

    private PatientMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PatientMapper();
    }

    @Test
    void toDTO_comPatientNulo_retornaNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }

    @Test
    void toDTO_comPatientValido_mapeiaCorretamente() {
        UUID userId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(userId)
                .email("paciente@email.com")
                .password("senha")
                .build();

        Patient patient = Patient.builder()
                .id(patientId)
                .user(user)
                .name("João Silva")
                .cpf("123.456.789-00")
                .phone("11999999999")
                .birthDate(LocalDate.of(1990, 1, 15))
                .address("Rua das Flores, 10")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        PatientResponseDTO dto = mapper.toDTO(patient);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(patientId);
        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.email()).isEqualTo("paciente@email.com");
        assertThat(dto.name()).isEqualTo("João Silva");
        assertThat(dto.cpf()).isEqualTo("123.456.789-00");
        assertThat(dto.phone()).isEqualTo("11999999999");
        assertThat(dto.birthDate()).isEqualTo(LocalDate.of(1990, 1, 15));
        assertThat(dto.address()).isEqualTo("Rua das Flores, 10");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
}