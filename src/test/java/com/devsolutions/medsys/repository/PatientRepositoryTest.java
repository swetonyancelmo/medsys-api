package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Patient;
import com.devsolutions.medsys.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PatientRepository patientRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = em.persist(User.builder().email("patient@test.com").password("pass").build());
    }

    @Test
    void findByUserId_deveRetornarPacienteQuandoUserIdExiste() {
        em.persist(Patient.builder().user(user).name("Maria Silva").cpf("123.456.789-00").build());
        em.flush();

        Optional<Patient> result = patientRepository.findByUserId(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Maria Silva");
    }

    @Test
    void findByUserId_deveRetornarVazioQuandoUserIdNaoExiste() {
        Optional<Patient> result = patientRepository.findByUserId(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void findByCpf_deveRetornarPacienteQuandoCpfExiste() {
        em.persist(Patient.builder().user(user).name("Maria Silva").cpf("123.456.789-00").build());
        em.flush();

        Optional<Patient> result = patientRepository.findByCpf("123.456.789-00");

        assertThat(result).isPresent();
        assertThat(result.get().getCpf()).isEqualTo("123.456.789-00");
    }

    @Test
    void findByCpf_deveRetornarVazioQuandoCpfNaoExiste() {
        Optional<Patient> result = patientRepository.findByCpf("000.000.000-00");

        assertThat(result).isEmpty();
    }
}