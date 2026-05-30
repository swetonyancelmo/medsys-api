package com.devsolutions.medsys.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private MethodArgumentTypeMismatchException typeMismatchException;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleResourceNotFound_retorna404ComMensagem() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/patients/123");

        var response = handler.handleResourceNotFound(
                new ResourceNotFoundException("Paciente não encontrado."), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("Paciente não encontrado.");
        assertThat(response.getBody().path()).isEqualTo("/patients/123");
    }

    @Test
    void handleBusinessException_retorna409ComMensagem() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/appointments");

        var response = handler.handleBusinessException(
                new BusinessException("Médico indisponível neste horário."), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().message()).isEqualTo("Médico indisponível neste horário.");
    }

    @Test
    void handleBadCredentials_retorna401ComMensagemPadrao() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/auth/login");

        var response = handler.handleBadCredentialsException(
                new BadCredentialsException("credenciais erradas"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(401);
        assertThat(response.getBody().message()).isEqualTo("Credenciais inválidas.");
    }

    @Test
    void handleIllegalArgument_retorna400ComMensagem() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/auth/register/patient");

        var response = handler.handleIllegalArgumentException(
                new IllegalArgumentException("E-mail já cadastrado."), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().message()).isEqualTo("E-mail já cadastrado.");
    }

    @Test
    void handleValidationException_retorna400ComCampos() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/patients");

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "patientRequestDTO");
        bindingResult.addError(new FieldError("patientRequestDTO", "name", "não pode ser vazio"));
        bindingResult.addError(new FieldError("patientRequestDTO", "cpf", "inválido"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        var response = handler.handleValidationException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().fields()).containsEntry("name", "não pode ser vazio");
        assertThat(response.getBody().fields()).containsEntry("cpf", "inválido");
    }

    @Test
    void handleTypeMismatch_retorna400ComNomeDoParametro() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/appointments");
        when(typeMismatchException.getName()).thenReturn("status");

        var response = handler.handleTypeMismatchException(typeMismatchException, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Parâmetro inválido: status");
    }

    @Test
    void handleGenericException_retorna500() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/any");

        var response = handler.handleGenericException(new RuntimeException("erro inesperado"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().message()).isEqualTo("Erro interno inesperado.");
    }
}