package com.devsolutions.medsys.dto.prescription;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoPrescriptionTest {

    // ─────────────────────────────────────────────────────────────────────────
    // PrescriptionRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void prescriptionRequestDTO_criacao_e_acessores() {
        UUID appointmentId = UUID.randomUUID();
        LocalDate expiresAt = LocalDate.now().plusDays(30);
        PrescriptionRequestDTO dto = new PrescriptionRequestDTO(
                appointmentId, "Tomar amoxicilina", "Amoxicilina 500mg", expiresAt);

        assertThat(dto.appointmentId()).isEqualTo(appointmentId);
        assertThat(dto.description()).isEqualTo("Tomar amoxicilina");
        assertThat(dto.medications()).isEqualTo("Amoxicilina 500mg");
        assertThat(dto.expiresAt()).isEqualTo(expiresAt);
    }

    @Test
    void prescriptionRequestDTO_semDataDeExpiracao() {
        UUID appointmentId = UUID.randomUUID();
        PrescriptionRequestDTO dto = new PrescriptionRequestDTO(
                appointmentId, "Descanso", "Dipirona 1g", null);

        assertThat(dto.expiresAt()).isNull();
    }

    @Test
    void prescriptionRequestDTO_equals_e_hashCode() {
        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 12, 31);
        PrescriptionRequestDTO a = new PrescriptionRequestDTO(id, "D", "M", date);
        PrescriptionRequestDTO b = new PrescriptionRequestDTO(id, "D", "M", date);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void prescriptionRequestDTO_toString_naoNulo() {
        UUID id = UUID.randomUUID();
        PrescriptionRequestDTO dto = new PrescriptionRequestDTO(id, "Desc", "Med", null);
        assertThat(dto.toString()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PrescriptionResponseDTO (complementa cobertura do mapper)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void prescriptionResponseDTO_criacao_e_acessores() {
        UUID id = UUID.randomUUID();
        UUID appointmentId = UUID.randomUUID();
        LocalDate expiresAt = LocalDate.of(2026, 6, 30);
        LocalDateTime createdAt = LocalDateTime.now();

        PrescriptionResponseDTO dto = new PrescriptionResponseDTO(
                id, appointmentId, "Repouso absoluto", "Ibuprofeno 400mg", expiresAt, createdAt);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.appointmentId()).isEqualTo(appointmentId);
        assertThat(dto.description()).isEqualTo("Repouso absoluto");
        assertThat(dto.medications()).isEqualTo("Ibuprofeno 400mg");
        assertThat(dto.expiresAt()).isEqualTo(expiresAt);
        assertThat(dto.createdAt()).isEqualTo(createdAt);
    }

    @Test
    void prescriptionResponseDTO_equals_e_hashCode() {
        UUID id = UUID.randomUUID();
        UUID apId = UUID.randomUUID();
        LocalDate d = LocalDate.of(2026, 1, 1);
        LocalDateTime dt = LocalDateTime.now();
        PrescriptionResponseDTO a = new PrescriptionResponseDTO(id, apId, "D", "M", d, dt);
        PrescriptionResponseDTO b = new PrescriptionResponseDTO(id, apId, "D", "M", d, dt);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
