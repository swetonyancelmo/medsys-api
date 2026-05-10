package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {

    Optional<Consulta> findByMedicoIdAndDataHora(UUID medicoId, LocalDateTime dataHora);

    List<Consulta> findByMedicoIdAndDataHoraBetween(UUID medicoId, LocalDateTime inicio, LocalDateTime fim);

    List<Consulta> findByPacienteIdOrderByDataHoraDesc(UUID pacienteId);

    List<Consulta> findByDataHoraBetweenOrderByDataHoraAsc(LocalDateTime inicio, LocalDateTime fim);
}
