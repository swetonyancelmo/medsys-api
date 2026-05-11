package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, UUID> {

    Optional<DoctorAvailability> findByDoctorIdAndDayOfWeek(UUID doctorId, Integer dayOfWeek);

    List<DoctorAvailability> findByDoctorIdAndActiveTrue(UUID doctorId);
}
