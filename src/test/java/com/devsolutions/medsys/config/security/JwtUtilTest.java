package com.devsolutions.medsys.config.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET =
            "dGhpcy1pcy1hLXZlcnktc2VjcmV0LWtleS1mb3ItbWVkc3lzLWp3dC0yNTZiaXRz";
    private static final long EXPIRATION_MS = 86_400_000L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", EXPIRATION_MS);
    }

    @Test
    void generateToken_retornaTokenNaoNulo() {
        String token = jwtUtil.generateToken("medico@email.com", List.of("ROLE_DOCTOR"));

        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void extractEmail_retornaEmailCorreto() {
        String email = "paciente@email.com";
        String token = jwtUtil.generateToken(email, List.of("ROLE_PATIENT"));

        assertThat(jwtUtil.extractEmail(token)).isEqualTo(email);
    }

    @Test
    void extractRoles_retornaListaDeRolesCorretamente() {
        List<String> roles = List.of("ROLE_ATENDENTE", "ROLE_DOCTOR");
        String token = jwtUtil.generateToken("user@email.com", roles);

        assertThat(jwtUtil.extractRoles(token)).containsExactlyInAnyOrderElementsOf(roles);
    }

    @Test
    void isTokenValid_comEmailCorreto_retornaTrue() {
        String email = "atendente@medsys.com";
        String token = jwtUtil.generateToken(email, List.of("ROLE_ATENDENTE"));

        assertThat(jwtUtil.isTokenValid(token, email)).isTrue();
    }

    @Test
    void isTokenValid_comEmailErrado_retornaFalse() {
        String token = jwtUtil.generateToken("correto@email.com", List.of("ROLE_PATIENT"));

        assertThat(jwtUtil.isTokenValid(token, "outro@email.com")).isFalse();
    }

    @Test
    void isTokenValid_comUserDetails_retornaTrue() {
        String email = "doctor@medsys.com";
        String token = jwtUtil.generateToken(email, List.of("ROLE_DOCTOR"));

        UserDetails userDetails = User.withUsername(email).password("senha").roles("DOCTOR").build();

        assertThat(jwtUtil.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void isTokenValid_comTokenExpirado_lancaExcecao() {
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", -1000L);
        String token = jwtUtil.generateToken("user@email.com", List.of("ROLE_PATIENT"));

        assertThatThrownBy(() -> jwtUtil.isTokenValid(token, "user@email.com"))
                .isInstanceOf(Exception.class);
    }
}