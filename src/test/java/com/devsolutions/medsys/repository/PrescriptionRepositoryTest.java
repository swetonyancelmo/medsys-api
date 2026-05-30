package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.enums.AppointmentStatus;
import com.devsolutions.medsys.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PrescriptionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    private Appointment appointment;

    @BeforeEach
    void setUp() {
        User doctorUser = em.persist(User.builder().email("doctor@test.com").password("pass").build());
        User patientUser = em.persist(User.builder().email("patient@test.com").password("pass").build());
        Specialty specialty = em.persist(Specialty.builder().name("Cardiologia").build());
        Doctor doctor = em.persist(Doctor.builder().user(doctorUser).specialty(specialty).name("Dr. Joao").crm("CRM-12345").build());
        Patient patient = em.persist(Patient.builder().user(patientUser).name("Maria Silva").cpf("123.456.789-00").build());
        appointment = em.persist(Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .scheduledAt(LocalDateTime.of(2026, 6, 10, 9, 0))
                .status(AppointmentStatus.COMPLETED)
                .build());
    }

    @Test
    void findByAppointmentId_deveRetornarReceitaQuandoExiste() {
        em.persist(Prescription.builder()
                .appointment(appointment)
                .description("Tomar repouso")
                .medications("Dipirona 500mg")
                .build());
        em.flush();

        Optional<Prescription> result = prescriptionRepository.findByAppointmentId(appointment.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getMedications()).isEqualTo("Dipirona 500mg");
    }

    @Test
    void findByAppointmentId_deveRetornarVazioQuandoNaoHaReceita() {
        em.flush();

        Optional<Prescription> result = prescriptionRepository.findByAppointmentId(appointment.getId());

        assertThat(result).isEmpty();
    }
}