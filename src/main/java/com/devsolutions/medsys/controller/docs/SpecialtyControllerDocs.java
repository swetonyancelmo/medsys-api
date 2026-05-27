package com.devsolutions.medsys.controller.docs;

import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyRequestDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "Especialidades", description = "Consulta de especialidades médicas e médicos por especialidade")
public interface SpecialtyControllerDocs {

    @Operation(summary = "Criar especialidade", description = "Cadastra uma nova especialidade médica. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Especialidade criada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SpecialtyResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "409", description = "Especialidade já cadastrada com esse nome", content = @Content)
    })
    ResponseEntity<SpecialtyResponseDTO> create(@RequestBody SpecialtyRequestDTO dto);

    @Operation(summary = "Listar especialidades", description = "Retorna todas as especialidades. Filtro opcional por nome (busca parcial, case-insensitive). Requer ATENDENTE, DOCTOR ou PATIENT.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = SpecialtyResponseDTO.class))))
    ResponseEntity<List<SpecialtyResponseDTO>> findAll(@RequestParam(required = false) String name);

    @Operation(summary = "Buscar especialidade por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Especialidade encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SpecialtyResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Especialidade não encontrada", content = @Content)
    })
    ResponseEntity<SpecialtyResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Listar médicos por especialidade", description = "Retorna os médicos vinculados à especialidade informada. Requer ATENDENTE, DOCTOR ou PATIENT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DoctorResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Especialidade não encontrada", content = @Content)
    })
    ResponseEntity<List<DoctorResponseDTO>> findDoctorsBySpecialty(@PathVariable UUID id);
}
