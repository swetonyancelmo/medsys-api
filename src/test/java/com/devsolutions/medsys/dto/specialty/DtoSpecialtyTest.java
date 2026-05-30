package com.devsolutions.medsys.dto.specialty;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoSpecialtyTest {

    // ─────────────────────────────────────────────────────────────────────────
    // SpecialtyRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void specialtyRequestDTO_criacao_e_acessores() {
        SpecialtyRequestDTO dto = new SpecialtyRequestDTO("Cardiologia", "Especialidade do coração");

        assertThat(dto.name()).isEqualTo("Cardiologia");
        assertThat(dto.description()).isEqualTo("Especialidade do coração");
    }

    @Test
    void specialtyRequestDTO_comDescricaoNula() {
        SpecialtyRequestDTO dto = new SpecialtyRequestDTO("Neurologia", null);

        assertThat(dto.name()).isEqualTo("Neurologia");
        assertThat(dto.description()).isNull();
    }

    @Test
    void specialtyRequestDTO_equals_e_hashCode() {
        SpecialtyRequestDTO a = new SpecialtyRequestDTO("Ortopedia", "Ossos");
        SpecialtyRequestDTO b = new SpecialtyRequestDTO("Ortopedia", "Ossos");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void specialtyRequestDTO_toString_contem_nome() {
        SpecialtyRequestDTO dto = new SpecialtyRequestDTO("Pediatria", "Crianças");
        assertThat(dto.toString()).contains("Pediatria");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SpecialtyResponseDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void specialtyResponseDTO_criacao_e_acessores() {
        UUID id = UUID.randomUUID();
        SpecialtyResponseDTO dto = new SpecialtyResponseDTO(id, "Dermatologia", "Pele");

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.name()).isEqualTo("Dermatologia");
        assertThat(dto.description()).isEqualTo("Pele");
    }

    @Test
    void specialtyResponseDTO_equals_e_hashCode() {
        UUID id = UUID.randomUUID();
        SpecialtyResponseDTO a = new SpecialtyResponseDTO(id, "Ginecologia", "Feminino");
        SpecialtyResponseDTO b = new SpecialtyResponseDTO(id, "Ginecologia", "Feminino");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void specialtyResponseDTO_toString_naoNulo() {
        UUID id = UUID.randomUUID();
        SpecialtyResponseDTO dto = new SpecialtyResponseDTO(id, "Urologia", null);
        assertThat(dto.toString()).isNotNull();
    }
}
