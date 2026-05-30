package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.enums.AppointmentStatus;
import com.devsolutions.medsys.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Doctor doctor;
    private Patient patient;

    // Ponto de referência: terça-feira, 10/06/2026 às 09:00
    private static final LocalDateTime BASE_TIME = LocalDateTime.of(2026, 6, 10, 9, 0);

    @BeforeEach
    void setUp() {
        User doctorUser = em.persist(User.builder()
                .email("doctor@test.com")
                .password("pass")
                .build());

        User patientUser = em.persist(User.builder()
                .email("patient@test.com")
                .password("pass")
                .build());

        Specialty specialty = em.persist(Specialty.builder()
                .name("Cardiologia")
                .build());

        doctor = em.persist(Doctor.builder()
                .user(doctorUser)
                .specialty(specialty)
                .name("Dr. João")
                .crm("CRM-12345")
                .build());

        patient = em.persist(Patient.builder()
                .user(patientUser)
                .name("Maria Silva")
                .cpf("123.456.789-00")
                .build());
    }

    private Appointment saveAppointment(LocalDateTime scheduledAt, AppointmentStatus status) {
        return em.persist(Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .scheduledAt(scheduledAt)
                .status(status)
                .build());
    }

    @Test
    void findByDoctorIdAndScheduledAtAndStatusNot_deveRetornarConsultaQuandoNaoCancelada() {
        saveAppointment(BASE_TIME, AppointmentStatus.SCHEDULED);
        em.flush();

        Optional<Appointment> result = appointmentRepository
                .findByDoctorIdAndScheduledAtAndStatusNot(
                        doctor.getId(), BASE_TIME, AppointmentStatus.CANCELLED);

        assertThat(result).isPresent();
        assertThat(result.get().getDoctor().getId()).isEqualTo(doctor.getId());
    }

    @Test
    void findByDoctorIdAndScheduledAtAndStatusNot_deveRetornarVazioQuandoCancelada() {
        saveAppointment(BASE_TIME, AppointmentStatus.CANCELLED);
        em.flush();

        Optional<Appointment> result = appointmentRepository
                .findByDoctorIdAndScheduledAtAndStatusNot(
                        doctor.getId(), BASE_TIME, AppointmentStatus.CANCELLED);

        assertThat(result).isEmpty();
    }

    @Test
    void findByPatientIdAndScheduledAtAndStatusNot_deveRetornarConsultaQuandoNaoCancelada() {
        saveAppointment(BASE_TIME, AppointmentStatus.SCHEDULED);
        em.flush();

        Optional<Appointment> result = appointmentRepository
                .findByPatientIdAndScheduledAtAndStatusNot(
                        patient.getId(), BASE_TIME, AppointmentStatus.CANCELLED);

        assertThat(result).isPresent();
        assertThat(result.get().getPatient().getId()).isEqualTo(patient.getId());
    }

    @Test
    void findByPatientIdAndScheduledAtAndStatusNot_deveRetornarVazioQuandoCancelada() {
        saveAppointment(BASE_TIME, AppointmentStatus.CANCELLED);
        em.flush();

        Optional<Appointment> result = appointmentRepository
                .findByPatientIdAndScheduledAtAndStatusNot(
                        patient.getId(), BASE_TIME, AppointmentStatus.CANCELLED);

        assertThat(result).isEmpty();
    }

    // --- findByDoctorIdAndScheduledAtBetween ---

    @Test
    void findByDoctorIdAndScheduledAtBetween_deveRetornarConsultasNoPeriodo() {
        saveAppointment(BASE_TIME, AppointmentStatus.SCHEDULED);           // dentro
        saveAppointment(BASE_TIME.plusHours(1), AppointmentStatus.SCHEDULED); // dentro
        saveAppointment(BASE_TIME.plusDays(2), AppointmentStatus.SCHEDULED);  // fora
        em.flush();

        List<Appointment> result = appointmentRepository.findByDoctorIdAndScheduledAtBetween(
                doctor.getId(),
                BASE_TIME.minusMinutes(1),
                BASE_TIME.plusHours(2));

        assertThat(result).hasSize(2);
    }

    @Test
    void findByDoctorIdAndScheduledAtBetween_deveRetornarVazioQuandoNenhumaNoIntervalo() {
        saveAppointment(BASE_TIME.plusDays(5), AppointmentStatus.SCHEDULED);
        em.flush();

        List<Appointment> result = appointmentRepository.findByDoctorIdAndScheduledAtBetween(
                doctor.getId(),
                BASE_TIME,
                BASE_TIME.plusHours(8));

        assertThat(result).isEmpty();
    }

    @Test
    void findByPatientIdOrderByScheduledAtDesc_deveRetornarTodasAsConsultasEmOrdemDecrescente() {
        saveAppointment(BASE_TIME, AppointmentStatus.SCHEDULED);
        saveAppointment(BASE_TIME.plusHours(2), AppointmentStatus.CONFIRMED);
        saveAppointment(BASE_TIME.minusHours(3), AppointmentStatus.CANCELLED);
        em.flush();

        List<Appointment> result = appointmentRepository
                .findByPatientIdOrderByScheduledAtDesc(patient.getId());

        assertThat(result).hasSize(3);

        assertThat(result.get(0).getScheduledAt()).isAfter(result.get(1).getScheduledAt());
        assertThat(result.get(1).getScheduledAt()).isAfter(result.get(2).getScheduledAt());
    }

    @Test
    void findByPatientIdOrderByScheduledAtDesc_naoDeveRetornarConsultasDeOutroPaciente() {
        saveAppointment(BASE_TIME, AppointmentStatus.SCHEDULED);
        em.flush();

        List<Appointment> result = appointmentRepository
                .findByPatientIdOrderByScheduledAtDesc(java.util.UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void findByScheduledAtBetweenOrderByScheduledAtAsc_deveRetornarConsultasNoPeriodoEmOrdemCrescente() {
        saveAppointment(BASE_TIME.minusHours(5), AppointmentStatus.SCHEDULED);
        saveAppointment(BASE_TIME, AppointmentStatus.SCHEDULED);
        saveAppointment(BASE_TIME.plusHours(2), AppointmentStatus.CONFIRMED);
        saveAppointment(BASE_TIME.plusHours(10), AppointmentStatus.SCHEDULED);
        em.flush();

        List<Appointment> result = appointmentRepository.findByScheduledAtBetweenOrderByScheduledAtAsc(
                BASE_TIME.minusHours(1),
                BASE_TIME.plusHours(3));

        assertThat(result).hasSize(2);

        assertThat(result.get(0).getScheduledAt()).isBefore(result.get(1).getScheduledAt());
    }

    @Test
    void findByScheduledAtBetweenOrderByScheduledAtAsc_deveRetornarVazioForaDoPeriodo() {
        saveAppointment(BASE_TIME.minusDays(1), AppointmentStatus.SCHEDULED);
        saveAppointment(BASE_TIME.plusDays(1), AppointmentStatus.SCHEDULED);
        em.flush();

        List<Appointment> result = appointmentRepository.findByScheduledAtBetweenOrderByScheduledAtAsc(
                BASE_TIME.minusHours(2),
                BASE_TIME.plusHours(2));

        assertThat(result).isEmpty();
    }
}
