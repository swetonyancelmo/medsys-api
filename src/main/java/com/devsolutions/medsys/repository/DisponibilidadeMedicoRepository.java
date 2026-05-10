package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.DisponibilidadeMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DisponibilidadeMedicoRepository extends JpaRepository<DisponibilidadeMedico, UUID> {
}
