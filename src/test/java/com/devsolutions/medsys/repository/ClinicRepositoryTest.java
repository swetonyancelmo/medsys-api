package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Clinic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClinicRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ClinicRepository clinicRepository;

    @Test
    void findByCnpj_deveRetornarClinicaQuandoCnpjExiste() {
        em.persist(Clinic.builder().name("Clinica Central").cnpj("12.345.678/0001-90").build());
        em.flush();

        Optional<Clinic> result = clinicRepository.findByCnpj("12.345.678/0001-90");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Clinica Central");
    }

    @Test
    void findByCnpj_deveRetornarVazioQuandoCnpjNaoExiste() {
        Optional<Clinic> result = clinicRepository.findByCnpj("00.000.000/0000-00");

        assertThat(result).isEmpty();
    }

    @Test
    void findByNameContainingIgnoreCase_deveRetornarClinicasQueContemOTermo() {
        em.persist(Clinic.builder().name("Clinica Central").cnpj("12.345.678/0001-90").build());
        em.persist(Clinic.builder().name("Clinica Norte").cnpj("98.765.432/0001-10").build());
        em.persist(Clinic.builder().name("Hospital Sao Lucas").cnpj("11.111.111/0001-11").build());
        em.flush();

        Page<Clinic> result = clinicRepository.findByNameContainingIgnoreCase("Clinica", Pageable.unpaged());

        assertThat(result).hasSize(2);
    }

    @Test
    void findByNameContainingIgnoreCase_deveIgnorarCase() {
        em.persist(Clinic.builder().name("Clinica Central").cnpj("12.345.678/0001-90").build());
        em.flush();

        Page<Clinic> result = clinicRepository.findByNameContainingIgnoreCase("CLINICA", Pageable.unpaged());

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Clinica Central");
    }
}