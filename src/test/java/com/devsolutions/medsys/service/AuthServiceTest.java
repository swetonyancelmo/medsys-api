package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.auth.AtendenteRegisterRequestDTO;
import com.devsolutions.medsys.dto.auth.DoctorRegisterRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.user.UserResponseDTO;

import com.devsolutions.medsys.service.AuthService;
import com.devsolutions.medsys.service.UserService;
import com.devsolutions.medsys.service.PatientService;
import com.devsolutions.medsys.service.DoctorService;

import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.devsolutions.medsys.model.User;
import com.devsolutions.medsys.dto.auth.PatientRegisterRequestDTO;
import com.devsolutions.medsys.config.security.JwtUtil;
import com.devsolutions.medsys.dto.auth.LoginRequestDTO;
import com.devsolutions.medsys.dto.auth.LoginResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserService userService;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void deveRealizarLoginComSucesso() {

        LoginRequestDTO request =
                new LoginRequestDTO("teste@email.com", "123");

        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);

        when(auth.getAuthorities())
                .thenReturn(List.of());

        when(jwtUtil.generateToken(anyString(), anyList()))
                .thenReturn("token123");

        LoginResponseDTO response =
                authService.login(request);

        assertNotNull(response);
        assertEquals("token123", response.token());
        assertEquals("teste@email.com", response.email());
    }
    @Test
    void deveLancarErroQuandoLoginFalhar() {

        LoginRequestDTO request =
                new LoginRequestDTO("teste@email.com", "senhaerrada");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Credenciais inválidas"));

        assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );
    }
    @Test
    void deveRegistrarPacienteComSucesso() {

        PatientRegisterRequestDTO request =
                mock(PatientRegisterRequestDTO.class);

        when(request.email()).thenReturn("paciente@email.com");
        when(request.password()).thenReturn("123");

        User user = User.builder()
                .id(java.util.UUID.randomUUID())
                .email("paciente@email.com")
                .build();

        PatientResponseDTO responseDTO =
                mock(PatientResponseDTO.class);

        when(userService.createUser(anyString(), anyString(), anyString()))
                .thenReturn(user);

        when(patientService.createFromUser(user, request))
                .thenReturn(responseDTO);

        PatientResponseDTO result =
                authService.registerPatient(request);

        assertNotNull(result);
        assertEquals(responseDTO, result);
    }
    @Test
    void deveRegistrarMedicoComSucesso() {

        DoctorRegisterRequestDTO request =
                mock(DoctorRegisterRequestDTO.class);

        when(request.email()).thenReturn("doctor@email.com");
        when(request.password()).thenReturn("123");

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("doctor@email.com")
                .build();

        DoctorResponseDTO responseDTO =
                mock(DoctorResponseDTO.class);

        when(userService.createUser(anyString(), anyString(), anyString()))
                .thenReturn(user);

        when(doctorService.createFromUser(user, request))
                .thenReturn(responseDTO);

        DoctorResponseDTO result =
                authService.registerDoctor(request);

        assertNotNull(result);
        assertEquals(responseDTO, result);
    }@Test
    void deveRegistrarAtendenteComSucesso() {

        AtendenteRegisterRequestDTO request =
                mock(AtendenteRegisterRequestDTO.class);

        when(request.email()).thenReturn("atendente@email.com");
        when(request.password()).thenReturn("123"); // 🔥 CORREÇÃO PRINCIPAL

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("atendente@email.com")
                .build();

        when(userService.createUser(
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(user);

        UserResponseDTO response =
                new UserResponseDTO(
                        user.getId(),
                        user.getEmail(),
                        user.isActive(),
                        user.getCreatedAt()
                );

        UserResponseDTO result =
                authService.registerAtendente(request);

        assertNotNull(result);
        assertEquals(response.email(), result.email());
        assertEquals(response.id(), result.id());
    }
}