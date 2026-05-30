package com.devsolutions.medsys.dto.clinic;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoClinicTest {

    // ─────────────────────────────────────────────────────────────────────────
    // ClinicRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void clinicRequestDTO_criacao_e_acessores() {
        ClinicRequestDTO dto = new ClinicRequestDTO(
                "Clínica Saúde Total", "12.345.678/0001-90",
                "11999998888", "Av. Paulista, 1000", "clinica@saude.com");

        assertThat(dto.name()).isEqualTo("Clínica Saúde Total");
        assertThat(dto.cnpj()).isEqualTo("12.345.678/0001-90");
        assertThat(dto.phone()).isEqualTo("11999998888");
        assertThat(dto.address()).isEqualTo("Av. Paulista, 1000");
        assertThat(dto.email()).isEqualTo("clinica@saude.com");
    }

    @Test
    void clinicRequestDTO_equals_e_hashCode() {
        ClinicRequestDTO a = new ClinicRequestDTO("N", "C", "P", "A", "E");
        ClinicRequestDTO b = new ClinicRequestDTO("N", "C", "P", "A", "E");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void clinicRequestDTO_comCamposOpcionaisNulos() {
        ClinicRequestDTO dto = new ClinicRequestDTO("Clínica X", "98.765.432/0001-10", null, null, null);

        assertThat(dto.phone()).isNull();
        assertThat(dto.address()).isNull();
        assertThat(dto.email()).isNull();
    }

    @Test
    void clinicRequestDTO_toString_naoNulo() {
        ClinicRequestDTO dto = new ClinicRequestDTO("N", "C", "P", "A", "E");
        assertThat(dto.toString()).isNotNull().contains("N");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ClinicResponseDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void clinicResponseDTO_criacao_e_acessores() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        ClinicResponseDTO dto = new ClinicResponseDTO(
                id, "Clínica Bem Estar", "11.222.333/0001-44",
                "11988887777", "Rua B, 20", "bemestar@clinica.com",
                true, now, now);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.name()).isEqualTo("Clínica Bem Estar");
        assertThat(dto.cnpj()).isEqualTo("11.222.333/0001-44");
        assertThat(dto.phone()).isEqualTo("11988887777");
        assertThat(dto.address()).isEqualTo("Rua B, 20");
        assertThat(dto.email()).isEqualTo("bemestar@clinica.com");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }

    @Test
    void clinicResponseDTO_equals_e_hashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        ClinicResponseDTO a = new ClinicResponseDTO(id, "N", "C", "P", "A", "E", true, now, now);
        ClinicResponseDTO b = new ClinicResponseDTO(id, "N", "C", "P", "A", "E", true, now, now);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void clinicResponseDTO_toString_naoNulo() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        ClinicResponseDTO dto = new ClinicResponseDTO(id, "N", "C", "P", "A", "E", false, now, now);
        assertThat(dto.toString()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ClinicUpdateDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void clinicUpdateDTO_criacao_e_acessores() {
        ClinicUpdateDTO dto = new ClinicUpdateDTO(
                "Novo Nome", "11999887766", "Nova Rua, 99", "novo@email.com");

        assertThat(dto.name()).isEqualTo("Novo Nome");
        assertThat(dto.phone()).isEqualTo("11999887766");
        assertThat(dto.address()).isEqualTo("Nova Rua, 99");
        assertThat(dto.email()).isEqualTo("novo@email.com");
    }

    @Test
    void clinicUpdateDTO_comTodosCamposNulos() {
        ClinicUpdateDTO dto = new ClinicUpdateDTO(null, null, null, null);

        assertThat(dto.name()).isNull();
        assertThat(dto.phone()).isNull();
        assertThat(dto.address()).isNull();
        assertThat(dto.email()).isNull();
    }

    @Test
    void clinicUpdateDTO_equals_e_hashCode() {
        ClinicUpdateDTO a = new ClinicUpdateDTO("N", "P", "A", "E");
        ClinicUpdateDTO b = new ClinicUpdateDTO("N", "P", "A", "E");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void clinicUpdateDTO_toString_naoNulo() {
        ClinicUpdateDTO dto = new ClinicUpdateDTO("N", "P", "A", "E");
        assertThat(dto.toString()).isNotNull().contains("N");
    }
}
