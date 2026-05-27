package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {

    List<Specialty> findByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
