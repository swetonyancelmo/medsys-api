package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, UUID> {

    Optional<Medico> findByUsuarioId(UUID usuarioId);

    Optional<Medico> findByCrm(String crm);

    List<Medico> findByEspecialidadeIdAndAtivoTrue(UUID especialidadeId);

}