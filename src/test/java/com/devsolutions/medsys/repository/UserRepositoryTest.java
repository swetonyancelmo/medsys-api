package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_deveRetornarUsuarioQuandoEmailExiste() {
        em.persist(User.builder().email("joao@test.com").password("pass").build());
        em.flush();

        Optional<User> result = userRepository.findByEmail("joao@test.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("joao@test.com");
    }

    @Test
    void findByEmail_deveRetornarVazioQuandoEmailNaoExiste() {
        Optional<User> result = userRepository.findByEmail("naoexiste@test.com");

        assertThat(result).isEmpty();
    }
}