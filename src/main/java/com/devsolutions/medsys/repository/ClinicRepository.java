package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, UUID> {

    Optional<Clinic> findByCnpj(String cnpj);

    Page<Clinic> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
