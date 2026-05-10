package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TelefoneRepository extends JpaRepository<Telefone, UUID> {

    List<Telefone> findByUsuarioId(UUID usuarioId);
}
