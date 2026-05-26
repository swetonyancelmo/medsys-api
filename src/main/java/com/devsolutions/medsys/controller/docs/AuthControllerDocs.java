package com.devsolutions.medsys.controller.docs;

import com.devsolutions.medsys.dto.auth.AtendenteRegisterRequestDTO;
import com.devsolutions.medsys.dto.auth.DoctorRegisterRequestDTO;
import com.devsolutions.medsys.dto.auth.LoginRequestDTO;
import com.devsolutions.medsys.dto.auth.LoginResponseDTO;
import com.devsolutions.medsys.dto.auth.PatientRegisterRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {

    @Operation(summary = "Login", description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciais incorretas",
                    content = @Content)
    })
    ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto);

    @Operation(summary = "Cadastro de paciente",
            description = "Registra um novo paciente com perfil PATIENT. Endpoint público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                    content = @Content)
    })
    ResponseEntity<PatientResponseDTO> registerPatient(@Valid @RequestBody PatientRegisterRequestDTO dto);

    @Operation(summary = "Cadastro de médico",
            description = "Registra um novo médico com perfil DOCTOR. Requer autenticação com perfil ATENDENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão (requer ATENDENTE)",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail ou CRM já cadastrado",
                    content = @Content)
    })
    ResponseEntity<DoctorResponseDTO> registerDoctor(@Valid @RequestBody DoctorRegisterRequestDTO dto);

    @Operation(summary = "Cadastro de atendente",
            description = "Registra um novo atendente com perfil ATENDENTE. Requer autenticação com perfil ATENDENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atendente cadastrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão (requer ATENDENTE)",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                    content = @Content)
    })
    ResponseEntity<UserResponseDTO> registerAtendente(@Valid @RequestBody AtendenteRegisterRequestDTO dto);
}