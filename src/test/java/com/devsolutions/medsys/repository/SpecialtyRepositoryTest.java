package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Specialty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SpecialtyRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Test
    void findByNameContainingIgnoreCase_deveRetornarEspecialidadesQueContemOTermo() {
        em.persist(Specialty.builder().name("Cardiologia").build());
        em.persist(Specialty.builder().name("Neurologia").build());
        em.persist(Specialty.builder().name("Pediatria").build());
        em.flush();

        List<Specialty> result = specialtyRepository.findByNameContainingIgnoreCase("ologia");

        assertThat(result).hasSize(2);
    }

    @Test
    void findByNameContainingIgnoreCase_deveIgnorarCase() {
        em.persist(Specialty.builder().name("Cardiologia").build());
        em.flush();

        List<Specialty> result = specialtyRepository.findByNameContainingIgnoreCase("CARDIO");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Cardiologia");
    }

    @Test
    void existsByNameIgnoreCase_deveRetornarTrueQuandoNomeExiste() {
        em.persist(Specialty.builder().name("Cardiologia").build());
        em.flush();

        boolean exists = specialtyRepository.existsByNameIgnoreCase("cardiologia");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByNameIgnoreCase_deveRetornarFalseQuandoNomeNaoExiste() {
        boolean exists = specialtyRepository.existsByNameIgnoreCase("Dermatologia");

        assertThat(exists).isFalse();
    }
}