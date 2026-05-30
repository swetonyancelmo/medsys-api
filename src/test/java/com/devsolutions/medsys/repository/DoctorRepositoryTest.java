package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.Specialty;
import com.devsolutions.medsys.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoctorRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private DoctorRepository doctorRepository;

    private User user;
    private Specialty specialty;

    @BeforeEach
    void setUp() {
        user = em.persist(User.builder().email("doctor@test.com").password("pass").build());
        specialty = em.persist(Specialty.builder().name("Cardiologia").build());
    }

    @Test
    void findByUserId_deveRetornarMedicoQuandoUserIdExiste() {
        em.persist(Doctor.builder().user(user).specialty(specialty).name("Dr. Joao").crm("CRM-12345").build());
        em.flush();

        Optional<Doctor> result = doctorRepository.findByUserId(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Dr. Joao");
    }

    @Test
    void findByUserId_deveRetornarVazioQuandoUserIdNaoExiste() {
        Optional<Doctor> result = doctorRepository.findByUserId(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void findByCrm_deveRetornarMedicoQuandoCrmExiste() {
        em.persist(Doctor.builder().user(user).specialty(specialty).name("Dr. Joao").crm("CRM-12345").build());
        em.flush();

        Optional<Doctor> result = doctorRepository.findByCrm("CRM-12345");

        assertThat(result).isPresent();
        assertThat(result.get().getCrm()).isEqualTo("CRM-12345");
    }

    @Test
    void findByCrm_deveRetornarVazioQuandoCrmNaoExiste() {
        Optional<Doctor> result = doctorRepository.findByCrm("CRM-99999");

        assertThat(result).isEmpty();
    }

    @Test
    void findBySpecialtyIdAndActiveTrue_deveRetornarApenasAtivos() {
        User user2 = em.persist(User.builder().email("doctor2@test.com").password("pass").build());
        em.persist(Doctor.builder().user(user).specialty(specialty).name("Dr. Joao").crm("CRM-12345").active(true).build());
        em.persist(Doctor.builder().user(user2).specialty(specialty).name("Dr. Carlos").crm("CRM-99999").active(false).build());
        em.flush();

        List<Doctor> result = doctorRepository.findBySpecialtyIdAndActiveTrue(specialty.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCrm()).isEqualTo("CRM-12345");
    }

    @Test
    void findBySpecialtyIdAndActiveTrue_deveRetornarVazioQuandoEspecialidadeNaoExiste() {
        List<Doctor> result = doctorRepository.findBySpecialtyIdAndActiveTrue(UUID.randomUUID());

        assertThat(result).isEmpty();
    }
}