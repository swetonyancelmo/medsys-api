package com.devsolutions.medsys.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    void businessException_propagaMensagem() {
        BusinessException ex = new BusinessException("Horário já ocupado.");
        assertThat(ex.getMessage()).isEqualTo("Horário já ocupado.");
    }

    @Test
    void resourceNotFoundException_propagaMensagem() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Paciente não encontrado.");
        assertThat(ex.getMessage()).isEqualTo("Paciente não encontrado.");
    }

    @Test
    void apiErrorResponse_of_criaComFieldsNulos() {
        ApiErrorResponse response = ApiErrorResponse.of(404, "Not Found", "Recurso não encontrado.", "/patients/1");

        assertThat(response.status()).isEqualTo(404);
        assertThat(response.error()).isEqualTo("Not Found");
        assertThat(response.message()).isEqualTo("Recurso não encontrado.");
        assertThat(response.path()).isEqualTo("/patients/1");
        assertThat(response.fields()).isNull();
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    void apiErrorResponse_withFields_criaComCamposDeValidacao() {
        Map<String, String> fields = Map.of("name", "não pode ser vazio", "cpf", "inválido");

        ApiErrorResponse response = ApiErrorResponse.withFields(400, "Bad Request", "Requisição inválida.", "/patients", fields);

        assertThat(response.status()).isEqualTo(400);
        assertThat(response.error()).isEqualTo("Bad Request");
        assertThat(response.message()).isEqualTo("Requisição inválida.");
        assertThat(response.path()).isEqualTo("/patients");
        assertThat(response.fields()).containsEntry("name", "não pode ser vazio");
        assertThat(response.fields()).containsEntry("cpf", "inválido");
        assertThat(response.timestamp()).isNotNull();
    }
}