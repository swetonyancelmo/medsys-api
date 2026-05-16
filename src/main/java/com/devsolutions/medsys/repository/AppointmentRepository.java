package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.enums.AppointmentStatus;
import com.devsolutions.medsys.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Optional<Appointment> findByDoctorIdAndScheduledAtAndStatusNot(
            UUID doctorId, LocalDateTime scheduledAt, AppointmentStatus status);

    Optional<Appointment> findByPatientIdAndScheduledAtAndStatusNot(
            UUID patientId, LocalDateTime scheduledAt, AppointmentStatus status);

    List<Appointment> findByDoctorIdAndScheduledAtBetween(UUID doctorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByPatientIdOrderByScheduledAtDesc(UUID patientId);

    List<Appointment> findByScheduledAtBetweenOrderByScheduledAtAsc(LocalDateTime start, LocalDateTime end);
}
