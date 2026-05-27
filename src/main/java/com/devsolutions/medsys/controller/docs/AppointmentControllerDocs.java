package com.devsolutions.medsys.controller.docs;

import com.devsolutions.medsys.dto.appointment.AppointmentRequestDTO;
import com.devsolutions.medsys.dto.appointment.AppointmentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Agendamentos", description = "Gerenciamento de consultas agendadas")
public interface AppointmentControllerDocs {

    @Operation(summary = "Agendar consulta", description = "Cria um novo agendamento validando disponibilidade e conflitos. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agendamento criado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppointmentResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de horário ou disponibilidade", content = @Content)
    })
    ResponseEntity<AppointmentResponseDTO> schedule(@Valid @RequestBody AppointmentRequestDTO dto);

    @Operation(summary = "Buscar agendamento por ID")
    ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Agendamentos do médico por período",
            description = "Lista agendamentos de um médico entre duas datas (ISO 8601). Requer ATENDENTE ou DOCTOR.")
    ResponseEntity<List<AppointmentResponseDTO>> findByDoctorAndDateRange(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);

    @Operation(summary = "Agendamentos do paciente", description = "Lista todos os agendamentos de um paciente em ordem cronológica decrescente.")
    ResponseEntity<List<AppointmentResponseDTO>> findByPatient(@PathVariable UUID id);

    @Operation(summary = "Agendamentos por período", description = "Lista todos os agendamentos em um intervalo de datas. Requer ATENDENTE.")
    ResponseEntity<List<AppointmentResponseDTO>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);

    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento com status SCHEDULED. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado"),
            @ApiResponse(responseCode = "409", description = "Status inválido para cancelamento", content = @Content)
    })
    ResponseEntity<AppointmentResponseDTO> cancel(@PathVariable UUID id);
}
