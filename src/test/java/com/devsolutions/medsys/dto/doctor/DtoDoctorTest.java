package com.devsolutions.medsys.dto.doctor;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoDoctorTest {

    // ─────────────────────────────────────────────────────────────────────────
    // DoctorRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void doctorRequestDTO_criacao_e_acessores() {
        UUID userId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();
        DoctorRequestDTO dto = new DoctorRequestDTO(
                userId, specialtyId, "Dr. Paulo", "CRM-54321", "11966665555", 45);

        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.specialtyId()).isEqualTo(specialtyId);
        assertThat(dto.name()).isEqualTo("Dr. Paulo");
        assertThat(dto.crm()).isEqualTo("CRM-54321");
        assertThat(dto.phone()).isEqualTo("11966665555");
        assertThat(dto.appointmentDurationMin()).isEqualTo(45);
    }

    @Test
    void doctorRequestDTO_comCamposOpcionaisNulos() {
        UUID userId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();
        DoctorRequestDTO dto = new DoctorRequestDTO(userId, specialtyId, "Dr. Ana", "CRM-11111", null, null);

        assertThat(dto.phone()).isNull();
        assertThat(dto.appointmentDurationMin()).isNull();
    }

    @Test
    void doctorRequestDTO_equals_e_hashCode() {
        UUID userId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();
        DoctorRequestDTO a = new DoctorRequestDTO(userId, specialtyId, "N", "C", "P", 30);
        DoctorRequestDTO b = new DoctorRequestDTO(userId, specialtyId, "N", "C", "P", 30);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void doctorRequestDTO_toString_naoNulo() {
        UUID userId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();
        DoctorRequestDTO dto = new DoctorRequestDTO(userId, specialtyId, "Dr. X", "CRM-0", "tel", 30);
        assertThat(dto.toString()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DoctorUpdateDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void doctorUpdateDTO_criacao_e_acessores() {
        UUID specialtyId = UUID.randomUUID();
        DoctorUpdateDTO dto = new DoctorUpdateDTO("Dr. Novo Nome", "11944443333", specialtyId, 60);

        assertThat(dto.name()).isEqualTo("Dr. Novo Nome");
        assertThat(dto.phone()).isEqualTo("11944443333");
        assertThat(dto.specialtyId()).isEqualTo(specialtyId);
        assertThat(dto.appointmentDurationMin()).isEqualTo(60);
    }

    @Test
    void doctorUpdateDTO_comTodosCamposNulos() {
        DoctorUpdateDTO dto = new DoctorUpdateDTO(null, null, null, null);

        assertThat(dto.name()).isNull();
        assertThat(dto.phone()).isNull();
        assertThat(dto.specialtyId()).isNull();
        assertThat(dto.appointmentDurationMin()).isNull();
    }

    @Test
    void doctorUpdateDTO_equals_e_hashCode() {
        UUID specialtyId = UUID.randomUUID();
        DoctorUpdateDTO a = new DoctorUpdateDTO("N", "P", specialtyId, 30);
        DoctorUpdateDTO b = new DoctorUpdateDTO("N", "P", specialtyId, 30);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void doctorUpdateDTO_toString_naoNulo() {
        DoctorUpdateDTO dto = new DoctorUpdateDTO("Nome", "Phone", null, 30);
        assertThat(dto.toString()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DoctorResponseDTO (complementa cobertura do mapper)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void doctorResponseDTO_criacao_completa() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        DoctorResponseDTO dto = new DoctorResponseDTO(
                id, userId, "dr@email.com", specialtyId, "Cardiologia",
                "Dr. José", "CRM-77777", "11911112222", 30, true, now, now);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.email()).isEqualTo("dr@email.com");
        assertThat(dto.specialtyId()).isEqualTo(specialtyId);
        assertThat(dto.specialtyName()).isEqualTo("Cardiologia");
        assertThat(dto.name()).isEqualTo("Dr. José");
        assertThat(dto.crm()).isEqualTo("CRM-77777");
        assertThat(dto.phone()).isEqualTo("11911112222");
        assertThat(dto.appointmentDurationMin()).isEqualTo(30);
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
}
