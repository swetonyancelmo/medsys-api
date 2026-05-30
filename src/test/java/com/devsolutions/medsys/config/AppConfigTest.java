package com.devsolutions.medsys.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest {

    @Test
    void passwordEncoder_retornaInstanciaBCrypt() {
        AppConfig config = new AppConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        assertThat(encoder).isNotNull();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    void passwordEncoder_codificaEVerificaSenhaCorretamente() {
        AppConfig config = new AppConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        String rawPassword = "Medsys@2026";

        String encoded = encoder.encode(rawPassword);

        assertThat(encoded).isNotEqualTo(rawPassword);
        assertThat(encoder.matches(rawPassword, encoded)).isTrue();
        assertThat(encoder.matches("senhaErrada", encoded)).isFalse();
    }
}