package com.devsolutions.medsys.controller.docs;

import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityRequestDTO;
import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityResponseDTO;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "Disponibilidade Médica", description = "Configuração da agenda de disponibilidade dos médicos")
public interface DoctorAvailabilityControllerDocs {

    @Operation(summary = "Registrar disponibilidade",
            description = "Cria ou atualiza a disponibilidade de um médico para um dia da semana. Requer ATENDENTE ou DOCTOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Disponibilidade registrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorAvailabilityResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Horário inválido (início >= fim)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado", content = @Content)
    })
    ResponseEntity<DoctorAvailabilityResponseDTO> registerAvailability(@Valid @RequestBody DoctorAvailabilityRequestDTO dto);

    @Operation(summary = "Listar disponibilidades ativas do médico")
    ResponseEntity<List<DoctorAvailabilityResponseDTO>> findActiveAvailabilitiesByDoctor(@PathVariable UUID doctorId);

    @Operation(summary = "Desativar disponibilidade", description = "Desativa uma disponibilidade pelo ID. Requer ATENDENTE ou DOCTOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disponibilidade desativada"),
            @ApiResponse(responseCode = "404", description = "Disponibilidade não encontrada", content = @Content)
    })
    ResponseEntity<Void> disableAvailability(@PathVariable UUID id);
}
