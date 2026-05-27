package com.devsolutions.medsys.controller.docs;

import com.devsolutions.medsys.dto.clinic.ClinicRequestDTO;
import com.devsolutions.medsys.dto.clinic.ClinicResponseDTO;
import com.devsolutions.medsys.dto.clinic.ClinicUpdateDTO;
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

@Tag(name = "Clínicas", description = "Gerenciamento de clínicas e laboratórios")
public interface ClinicControllerDocs {

    @Operation(summary = "Cadastrar clínica", description = "Cria um novo registro de clínica ou laboratório. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Clínica criada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClinicResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado", content = @Content)
    })
    ResponseEntity<ClinicResponseDTO> create(@Valid @RequestBody ClinicRequestDTO dto);

    @Operation(summary = "Listar clínicas", description = "Retorna lista paginada de clínicas. Filtro opcional por nome. Requer ATENDENTE, DOCTOR ou PATIENT.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    ResponseEntity<Page<ClinicResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "12") @Positive Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction);

    @Operation(summary = "Buscar clínica por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clínica encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClinicResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Clínica não encontrada", content = @Content)
    })
    ResponseEntity<ClinicResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Atualizar clínica", description = "Atualiza campos da clínica. Campos nulos são ignorados. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clínica atualizada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClinicResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Clínica não encontrada", content = @Content)
    })
    ResponseEntity<ClinicResponseDTO> update(@PathVariable UUID id, @RequestBody ClinicUpdateDTO dto);

    @Operation(summary = "Excluir clínica", description = "Remove a clínica pelo ID. Requer ATENDENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Clínica excluída"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Clínica não encontrada", content = @Content)
    })
    ResponseEntity<Void> delete(@PathVariable UUID id);
}
