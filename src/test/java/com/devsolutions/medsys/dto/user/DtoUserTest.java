package com.devsolutions.medsys.dto.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoUserTest {

    @Test
    void userResponseDTO_criacao_e_acessores() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        UserResponseDTO dto = new UserResponseDTO(id, "usuario@email.com", true, now);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.email()).isEqualTo("usuario@email.com");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
    }

    @Test
    void userResponseDTO_usuarioInativo() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        UserResponseDTO dto = new UserResponseDTO(id, "inativo@email.com", false, now);

        assertThat(dto.active()).isFalse();
    }

    @Test
    void userResponseDTO_equals_e_hashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        UserResponseDTO a = new UserResponseDTO(id, "a@b.com", true, now);
        UserResponseDTO b = new UserResponseDTO(id, "a@b.com", true, now);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void userResponseDTO_toString_contem_email() {
        UUID id = UUID.randomUUID();
        UserResponseDTO dto = new UserResponseDTO(id, "email@test.com", true, LocalDateTime.now());
        assertThat(dto.toString()).contains("email@test.com");
    }
}
