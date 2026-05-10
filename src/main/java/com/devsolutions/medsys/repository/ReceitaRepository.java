package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceitaRepository extends JpaRepository<Receita, UUID> {
}
