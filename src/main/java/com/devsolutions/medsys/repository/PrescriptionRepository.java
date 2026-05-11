package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    Optional<Prescription> findByAppointmentId(UUID appointmentId);
}
