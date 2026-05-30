package com.devsolutions.medsys.dto.doctorAvailability;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoDoctorAvailabilityTest {

    // ─────────────────────────────────────────────────────────────────────────
    // DoctorAvailabilityRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void doctorAvailabilityRequestDTO_criacao_e_acessores() {
        UUID doctorId = UUID.randomUUID();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(17, 0);
        DoctorAvailabilityRequestDTO dto = new DoctorAvailabilityRequestDTO(doctorId, 2, start, end);

        assertThat(dto.doctorId()).isEqualTo(doctorId);
        assertThat(dto.dayOfWeek()).isEqualTo(2);
        assertThat(dto.startTime()).isEqualTo(start);
        assertThat(dto.endTime()).isEqualTo(end);
    }

    @Test
    void doctorAvailabilityRequestDTO_todosOsDias() {
        UUID doctorId = UUID.randomUUID();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);

        for (int dia = 1; dia <= 7; dia++) {
            DoctorAvailabilityRequestDTO dto = new DoctorAvailabilityRequestDTO(doctorId, dia, start, end);
            assertThat(dto.dayOfWeek()).isEqualTo(dia);
        }
    }

    @Test
    void doctorAvailabilityRequestDTO_equals_e_hashCode() {
        UUID doctorId = UUID.randomUUID();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(17, 0);

        DoctorAvailabilityRequestDTO a = new DoctorAvailabilityRequestDTO(doctorId, 3, start, end);
        DoctorAvailabilityRequestDTO b = new DoctorAvailabilityRequestDTO(doctorId, 3, start, end);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void doctorAvailabilityRequestDTO_toString_naoNulo() {
        UUID doctorId = UUID.randomUUID();
        DoctorAvailabilityRequestDTO dto = new DoctorAvailabilityRequestDTO(
                doctorId, 1, LocalTime.of(7, 0), LocalTime.of(12, 0));
        assertThat(dto.toString()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DoctorAvailabilityResponseDTO (complementa cobertura do mapper)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void doctorAvailabilityResponseDTO_criacao_e_acessores() {
        UUID id = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(17, 0);

        DoctorAvailabilityResponseDTO dto = new DoctorAvailabilityResponseDTO(
                id, doctorId, 5, start, end, true);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.doctorId()).isEqualTo(doctorId);
        assertThat(dto.dayOfWeek()).isEqualTo(5);
        assertThat(dto.startTime()).isEqualTo(start);
        assertThat(dto.endTime()).isEqualTo(end);
        assertThat(dto.active()).isTrue();
    }

    @Test
    void doctorAvailabilityResponseDTO_inativo() {
        UUID id = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        DoctorAvailabilityResponseDTO dto = new DoctorAvailabilityResponseDTO(
                id, doctorId, 7, LocalTime.of(9, 0), LocalTime.of(13, 0), false);

        assertThat(dto.active()).isFalse();
    }
}
