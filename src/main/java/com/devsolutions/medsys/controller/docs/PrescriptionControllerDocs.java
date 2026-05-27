package com.devsolutions.medsys.controller.docs;

import com.devsolutions.medsys.dto.prescription.PrescriptionRequestDTO;
import com.devsolutions.medsys.dto.prescription.PrescriptionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Receitas", description = "Gerenciamento de receitas médicas")
public interface PrescriptionControllerDocs {

    @Operation(summary = "Emitir receita", description = "Cria uma receita para uma consulta. Apenas um por consulta. Requer DOCTOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Receita criada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PrescriptionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Já existe receita para esta consulta", content = @Content)
    })
    ResponseEntity<PrescriptionResponseDTO> create(@Valid @RequestBody PrescriptionRequestDTO dto);

    @Operation(summary = "Buscar receita por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PrescriptionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada", content = @Content)
    })
    ResponseEntity<PrescriptionResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Buscar receita pela consulta")
    ResponseEntity<PrescriptionResponseDTO> findByAppointmentId(@PathVariable UUID appointmentId);
}
