package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.DoctorAvailability;
import com.devsolutions.medsys.model.Specialty;
import com.devsolutions.medsys.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoctorAvailabilityRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        User user = em.persist(User.builder().email("doctor@test.com").password("pass").build());
        Specialty specialty = em.persist(Specialty.builder().name("Cardiologia").build());
        doctor = em.persist(Doctor.builder().user(user).specialty(specialty).name("Dr. Joao").crm("CRM-12345").build());
    }

    private DoctorAvailability saveAvailability(Integer dayOfWeek, boolean active) {
        return em.persist(DoctorAvailability.builder()
                .doctor(doctor)
                .dayOfWeek(dayOfWeek)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(18, 0))
                .active(active)
                .build());
    }

    @Test
    void findByDoctorIdAndDayOfWeek_deveRetornarDisponibilidadeParaDiaDaSemana() {
        saveAvailability(2, true);
        em.flush();

        Optional<DoctorAvailability> result = availabilityRepository
                .findByDoctorIdAndDayOfWeek(doctor.getId(), 2);

        assertThat(result).isPresent();
        assertThat(result.get().getDayOfWeek()).isEqualTo(2);
    }

    @Test
    void findByDoctorIdAndDayOfWeek_deveRetornarVazioParaDiaSemDisponibilidade() {
        saveAvailability(2, true);
        em.flush();

        Optional<DoctorAvailability> result = availabilityRepository
                .findByDoctorIdAndDayOfWeek(doctor.getId(), 5);

        assertThat(result).isEmpty();
    }

    @Test
    void findByDoctorIdAndDayOfWeekAndActiveTrue_deveRetornarQuandoAtiva() {
        saveAvailability(2, true);
        em.flush();

        Optional<DoctorAvailability> result = availabilityRepository
                .findByDoctorIdAndDayOfWeekAndActiveTrue(doctor.getId(), 2);

        assertThat(result).isPresent();
    }

    @Test
    void findByDoctorIdAndDayOfWeekAndActiveTrue_deveRetornarVazioQuandoInativa() {
        saveAvailability(5, false);
        em.flush();

        Optional<DoctorAvailability> result = availabilityRepository
                .findByDoctorIdAndDayOfWeekAndActiveTrue(doctor.getId(), 5);

        assertThat(result).isEmpty();
    }

    @Test
    void findByDoctorIdAndActiveTrue_deveRetornarTodasAsDisponibilidadesAtivas() {
        saveAvailability(2, true);
        saveAvailability(3, true);
        saveAvailability(4, false);
        em.flush();

        List<DoctorAvailability> result = availabilityRepository.findByDoctorIdAndActiveTrue(doctor.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    void findByDoctorIdAndActiveTrue_deveRetornarVazioParaMedicoSemDisponibilidade() {
        List<DoctorAvailability> result = availabilityRepository.findByDoctorIdAndActiveTrue(UUID.randomUUID());

        assertThat(result).isEmpty();
    }
}