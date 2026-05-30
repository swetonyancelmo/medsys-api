package com.devsolutions.medsys.dto.patient;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoPatientTest {

    // ─────────────────────────────────────────────────────────────────────────
    // PatientRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void patientRequestDTO_criacao_e_acessores() {
        UUID userId = UUID.randomUUID();
        LocalDate birthDate = LocalDate.of(1988, 3, 22);
        PatientRequestDTO dto = new PatientRequestDTO(
                userId, "Ana Souza", "987.654.321-00", "11977776666", birthDate, "Rua C, 30");

        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.name()).isEqualTo("Ana Souza");
        assertThat(dto.cpf()).isEqualTo("987.654.321-00");
        assertThat(dto.phone()).isEqualTo("11977776666");
        assertThat(dto.birthDate()).isEqualTo(birthDate);
        assertThat(dto.address()).isEqualTo("Rua C, 30");
    }

    @Test
    void patientRequestDTO_comCamposOpcionaisNulos() {
        UUID userId = UUID.randomUUID();
        PatientRequestDTO dto = new PatientRequestDTO(userId, "Carlos", "111.111.111-11", null, null, null);

        assertThat(dto.phone()).isNull();
        assertThat(dto.birthDate()).isNull();
        assertThat(dto.address()).isNull();
    }

    @Test
    void patientRequestDTO_equals_e_hashCode() {
        UUID userId = UUID.randomUUID();
        LocalDate d = LocalDate.of(2000, 1, 1);
        PatientRequestDTO a = new PatientRequestDTO(userId, "N", "C", "P", d, "A");
        PatientRequestDTO b = new PatientRequestDTO(userId, "N", "C", "P", d, "A");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void patientRequestDTO_toString_naoNulo() {
        UUID userId = UUID.randomUUID();
        PatientRequestDTO dto = new PatientRequestDTO(userId, "N", "C", "P", null, "A");
        assertThat(dto.toString()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PatientUpdateDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void patientUpdateDTO_criacao_e_acessores() {
        LocalDate birthDate = LocalDate.of(1992, 7, 10);
        PatientUpdateDTO dto = new PatientUpdateDTO("Novo Nome", "11955554444", birthDate, "Nova Rua, 50");

        assertThat(dto.name()).isEqualTo("Novo Nome");
        assertThat(dto.phone()).isEqualTo("11955554444");
        assertThat(dto.birthDate()).isEqualTo(birthDate);
        assertThat(dto.address()).isEqualTo("Nova Rua, 50");
    }

    @Test
    void patientUpdateDTO_comTodosCamposNulos() {
        PatientUpdateDTO dto = new PatientUpdateDTO(null, null, null, null);

        assertThat(dto.name()).isNull();
        assertThat(dto.phone()).isNull();
        assertThat(dto.birthDate()).isNull();
        assertThat(dto.address()).isNull();
    }

    @Test
    void patientUpdateDTO_equals_e_hashCode() {
        LocalDate d = LocalDate.of(1985, 5, 5);
        PatientUpdateDTO a = new PatientUpdateDTO("N", "P", d, "A");
        PatientUpdateDTO b = new PatientUpdateDTO("N", "P", d, "A");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void patientUpdateDTO_toString_naoNulo() {
        PatientUpdateDTO dto = new PatientUpdateDTO("Nome", null, null, "Endereço");
        assertThat(dto.toString()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PatientResponseDTO (complementa cobertura do mapper)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void patientResponseDTO_criacao_completa() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate birthDate = LocalDate.of(1990, 1, 15);
        LocalDateTime now = LocalDateTime.now();

        PatientResponseDTO dto = new PatientResponseDTO(
                id, userId, "pac@email.com", "João Silva",
                "123.456.789-00", "11999999999", birthDate,
                "Rua das Flores, 10", true, now, now);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.email()).isEqualTo("pac@email.com");
        assertThat(dto.name()).isEqualTo("João Silva");
        assertThat(dto.cpf()).isEqualTo("123.456.789-00");
        assertThat(dto.phone()).isEqualTo("11999999999");
        assertThat(dto.birthDate()).isEqualTo(birthDate);
        assertThat(dto.address()).isEqualTo("Rua das Flores, 10");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
}
