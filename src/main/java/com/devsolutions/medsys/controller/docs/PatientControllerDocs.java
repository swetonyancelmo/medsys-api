package com.devsolutions.medsys.controller.docs;

import com.devsolutions.medsys.dto.patient.PatientRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Pacientes", description = "Gerenciamento de pacientes")
public interface PatientControllerDocs {

    @Operation(summary = "Cadastrar paciente", description = "Cria um novo registro de paciente vinculado a um usuário existente. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    ResponseEntity<PatientResponseDTO> create(@Valid @RequestBody PatientRequestDTO dto);

    @Operation(summary = "Listar pacientes", description = "Retorna lista paginada de pacientes. Requer ATENDENTE.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    ResponseEntity<Page<PatientResponseDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "12") @Positive Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction);

    @Operation(summary = "Buscar paciente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado", content = @Content)
    })
    ResponseEntity<PatientResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Atualizar paciente", description = "Atualiza campos do paciente. Campos nulos são ignorados. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente atualizado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado", content = @Content)
    })
    ResponseEntity<PatientResponseDTO> update(@PathVariable UUID id, @RequestBody PatientUpdateDTO dto);

    @Operation(summary = "Excluir paciente", description = "Remove o paciente pelo ID. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paciente excluído"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado", content = @Content)
    })
    ResponseEntity<Void> delete(@PathVariable UUID id);
}
