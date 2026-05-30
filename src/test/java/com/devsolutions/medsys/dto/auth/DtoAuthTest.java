package com.devsolutions.medsys.dto.auth;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoAuthTest {

    // ─────────────────────────────────────────────────────────────────────────
    // LoginRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void loginRequestDTO_criacao_e_acessores() {
        LoginRequestDTO dto = new LoginRequestDTO("user@email.com", "senha123");

        assertThat(dto.email()).isEqualTo("user@email.com");
        assertThat(dto.password()).isEqualTo("senha123");
    }

    @Test
    void loginRequestDTO_equals_e_hashCode() {
        LoginRequestDTO a = new LoginRequestDTO("a@b.com", "pass");
        LoginRequestDTO b = new LoginRequestDTO("a@b.com", "pass");
        LoginRequestDTO c = new LoginRequestDTO("x@b.com", "pass");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isNotEqualTo(c);
    }

    @Test
    void loginRequestDTO_toString_contem_campos() {
        LoginRequestDTO dto = new LoginRequestDTO("u@e.com", "pw");
        assertThat(dto.toString()).contains("u@e.com");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LoginResponseDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void loginResponseDTO_criacao_e_acessores() {
        List<String> roles = List.of("ROLE_PATIENT");
        LoginResponseDTO dto = new LoginResponseDTO("jwt.token.aqui", "user@email.com", roles);

        assertThat(dto.token()).isEqualTo("jwt.token.aqui");
        assertThat(dto.email()).isEqualTo("user@email.com");
        assertThat(dto.roles()).containsExactly("ROLE_PATIENT");
    }

    @Test
    void loginResponseDTO_equals_e_hashCode() {
        List<String> roles = List.of("ROLE_DOCTOR");
        LoginResponseDTO a = new LoginResponseDTO("tok", "e@m.com", roles);
        LoginResponseDTO b = new LoginResponseDTO("tok", "e@m.com", roles);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // AtendenteRegisterRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void atendenteRegisterRequestDTO_criacao_e_acessores() {
        AtendenteRegisterRequestDTO dto = new AtendenteRegisterRequestDTO(
                "atendente@medsys.com", "Medsys@2026");

        assertThat(dto.email()).isEqualTo("atendente@medsys.com");
        assertThat(dto.password()).isEqualTo("Medsys@2026");
    }

    @Test
    void atendenteRegisterRequestDTO_equals_e_hashCode() {
        AtendenteRegisterRequestDTO a = new AtendenteRegisterRequestDTO("a@b.com", "pass1234");
        AtendenteRegisterRequestDTO b = new AtendenteRegisterRequestDTO("a@b.com", "pass1234");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void atendenteRegisterRequestDTO_toString_naoNulo() {
        AtendenteRegisterRequestDTO dto = new AtendenteRegisterRequestDTO("x@y.com", "abc12345");
        assertThat(dto.toString()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DoctorRegisterRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void doctorRegisterRequestDTO_criacao_e_acessores() {
        UUID specialtyId = UUID.randomUUID();
        DoctorRegisterRequestDTO dto = new DoctorRegisterRequestDTO(
                "medico@email.com", "Senha@1234", "Dr. Carlos", "CRM-99999",
                specialtyId, "11999999999", 30);

        assertThat(dto.email()).isEqualTo("medico@email.com");
        assertThat(dto.password()).isEqualTo("Senha@1234");
        assertThat(dto.name()).isEqualTo("Dr. Carlos");
        assertThat(dto.crm()).isEqualTo("CRM-99999");
        assertThat(dto.specialtyId()).isEqualTo(specialtyId);
        assertThat(dto.phone()).isEqualTo("11999999999");
        assertThat(dto.appointmentDurationMin()).isEqualTo(30);
    }

    @Test
    void doctorRegisterRequestDTO_equals_e_hashCode() {
        UUID id = UUID.randomUUID();
        DoctorRegisterRequestDTO a = new DoctorRegisterRequestDTO("a@b.com", "pw123456", "N", "C", id, "1", 30);
        DoctorRegisterRequestDTO b = new DoctorRegisterRequestDTO("a@b.com", "pw123456", "N", "C", id, "1", 30);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void doctorRegisterRequestDTO_comCamposOpcionaisNulos() {
        UUID specialtyId = UUID.randomUUID();
        DoctorRegisterRequestDTO dto = new DoctorRegisterRequestDTO(
                "medico@email.com", "Senha@1234", "Dr. Ana", "CRM-00001",
                specialtyId, null, null);

        assertThat(dto.phone()).isNull();
        assertThat(dto.appointmentDurationMin()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PatientRegisterRequestDTO
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    void patientRegisterRequestDTO_criacao_e_acessores() {
        LocalDate birthDate = LocalDate.of(1995, 6, 15);
        PatientRegisterRequestDTO dto = new PatientRegisterRequestDTO(
                "pac@email.com", "Senha@1234", "Maria Silva",
                "123.456.789-00", "11988887777", birthDate, "Rua A, 10");

        assertThat(dto.email()).isEqualTo("pac@email.com");
        assertThat(dto.password()).isEqualTo("Senha@1234");
        assertThat(dto.name()).isEqualTo("Maria Silva");
        assertThat(dto.cpf()).isEqualTo("123.456.789-00");
        assertThat(dto.phone()).isEqualTo("11988887777");
        assertThat(dto.birthDate()).isEqualTo(birthDate);
        assertThat(dto.address()).isEqualTo("Rua A, 10");
    }

    @Test
    void patientRegisterRequestDTO_equals_e_hashCode() {
        LocalDate d = LocalDate.of(1990, 1, 1);
        PatientRegisterRequestDTO a = new PatientRegisterRequestDTO("a@b.com", "pw123456", "N", "C", "t", d, "ad");
        PatientRegisterRequestDTO b = new PatientRegisterRequestDTO("a@b.com", "pw123456", "N", "C", "t", d, "ad");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void patientRegisterRequestDTO_comCamposOpcionaisNulos() {
        PatientRegisterRequestDTO dto = new PatientRegisterRequestDTO(
                "pac@email.com", "Senha@1234", "João", "111.222.333-44",
                null, null, null);

        assertThat(dto.phone()).isNull();
        assertThat(dto.birthDate()).isNull();
        assertThat(dto.address()).isNull();
    }
}
