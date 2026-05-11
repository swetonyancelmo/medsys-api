package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByUserId(UUID userId);

    Optional<Doctor> findByCrm(String crm);

    List<Doctor> findBySpecialtyIdAndActiveTrue(UUID specialtyId);
}
