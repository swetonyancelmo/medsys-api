package com.devsolutions.medsys.controller.docs;

import com.devsolutions.medsys.dto.doctor.DoctorRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.doctor.DoctorUpdateDTO;
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

@Tag(name = "Médicos", description = "Gerenciamento de médicos")
public interface DoctorControllerDocs {

    @Operation(summary = "Cadastrar médico", description = "Cria um novo registro de médico. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Médico criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário ou especialidade não encontrada", content = @Content)
    })
    ResponseEntity<DoctorResponseDTO> create(@Valid @RequestBody DoctorRequestDTO dto);

    @Operation(summary = "Listar médicos paginado")
    ResponseEntity<Page<DoctorResponseDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "12") @Positive Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction);

    @Operation(summary = "Buscar médico por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado", content = @Content)
    })
    ResponseEntity<DoctorResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Atualizar médico", description = "Atualiza campos do médico. Campos nulos são ignorados. Requer ATENDENTE.")
    ResponseEntity<DoctorResponseDTO> update(@PathVariable UUID id, @RequestBody DoctorUpdateDTO dto);

    @Operation(summary = "Excluir médico", description = "Remove o médico pelo ID. Requer ATENDENTE.")
    ResponseEntity<Void> delete(@PathVariable UUID id);
}
