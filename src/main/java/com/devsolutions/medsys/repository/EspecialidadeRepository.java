package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, UUID> {
}