package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName_deveRetornarRoleQuandoNomeExiste() {
        em.persist(Role.builder().name("PATIENT").build());
        em.flush();

        Optional<Role> result = roleRepository.findByName("PATIENT");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("PATIENT");
    }

    @Test
    void findByName_deveRetornarVazioQuandoNomeNaoExiste() {
        Optional<Role> result = roleRepository.findByName("INEXISTENTE");

        assertThat(result).isEmpty();
    }
}