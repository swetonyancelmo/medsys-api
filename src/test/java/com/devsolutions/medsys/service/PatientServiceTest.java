package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.auth.PatientRegisterRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientUpdateDTO;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.PatientMapper;
import com.devsolutions.medsys.model.Patient;
import com.devsolutions.medsys.model.User;
import com.devsolutions.medsys.repository.PatientRepository;
import com.devsolutions.medsys.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @InjectMocks
    private PatientService service;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientMapper patientMapper;

    // ---------------- CREATE ----------------

    @Test
    void deveCriarPacienteComSucesso() {

        UUID userId = UUID.randomUUID();

        User user = User.builder().id(userId).build();

        PatientRequestDTO dto = mock(PatientRequestDTO.class);

        when(dto.userId()).thenReturn(userId);
        when(dto.name()).thenReturn("Paciente Teste");
        when(dto.cpf()).thenReturn("12345678900");
        when(dto.phone()).thenReturn("999999");
        when(dto.birthDate()).thenReturn(null);
        when(dto.address()).thenReturn("Rua X");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Patient patientSaved = Patient.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();

        when(patientRepository.save(any())).thenReturn(patientSaved);

        PatientResponseDTO response = mock(PatientResponseDTO.class);
        when(patientMapper.toDTO(patientSaved)).thenReturn(response);

        PatientResponseDTO result = service.create(dto);

        assertNotNull(result);
        verify(patientRepository, times(1)).save(any());
    }

    // ---------------- CREATE FROM USER ----------------

    @Test
    void deveCriarPacienteFromUser() {

        User user = User.builder().id(UUID.randomUUID()).build();

        PatientRegisterRequestDTO dto = mock(PatientRegisterRequestDTO.class);

        when(dto.name()).thenReturn("Paciente Teste");
        when(dto.cpf()).thenReturn("12345678900");
        when(dto.phone()).thenReturn("999");
        when(dto.birthDate()).thenReturn(null);
        when(dto.address()).thenReturn("Rua X");

        Patient patientSaved = Patient.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();

        when(patientRepository.save(any())).thenReturn(patientSaved);

        PatientResponseDTO response = mock(PatientResponseDTO.class);
        when(patientMapper.toDTO(patientSaved)).thenReturn(response);

        PatientResponseDTO result = service.createFromUser(user, dto);

        assertNotNull(result);
    }

    // ---------------- FIND BY ID ----------------

    @Test
    void deveBuscarPacientePorId() {

        UUID id = UUID.randomUUID();

        Patient patient = Patient.builder().id(id).build();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        PatientResponseDTO response = mock(PatientResponseDTO.class);
        when(patientMapper.toDTO(patient)).thenReturn(response);

        PatientResponseDTO result = service.findById(id);

        assertNotNull(result);
    }

    @Test
    void deveLancarErroSePacienteNaoExiste() {

        UUID id = UUID.randomUUID();

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findById(id));
    }

    // ---------------- FIND ALL ----------------

    @Test
    void deveListarPacientes() {

        Patient patient = Patient.builder().id(UUID.randomUUID()).build();

        when(patientRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(patient)));

        when(patientMapper.toDTO(patient))
                .thenReturn(mock(PatientResponseDTO.class));

        Page<PatientResponseDTO> result =
                service.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    // ---------------- UPDATE ----------------

    @Test
    void deveAtualizarPaciente() {

        UUID id = UUID.randomUUID();

        Patient patient = Patient.builder()
                .id(id)
                .name("Antigo")
                .build();

        PatientUpdateDTO dto = mock(PatientUpdateDTO.class);

        when(dto.name()).thenReturn("Novo Nome");
        when(dto.phone()).thenReturn(null);
        when(dto.birthDate()).thenReturn(null);
        when(dto.address()).thenReturn(null);

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        when(patientRepository.save(any())).thenReturn(patient);

        when(patientMapper.toDTO(patient))
                .thenReturn(mock(PatientResponseDTO.class));

        PatientResponseDTO result = service.update(id, dto);

        assertNotNull(result);
        assertEquals("Novo Nome", patient.getName());
    }

    // ---------------- DELETE ----------------

    @Test
    void deveDeletarPaciente() {

        UUID id = UUID.randomUUID();

        Patient patient = Patient.builder().id(id).build();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        assertDoesNotThrow(() -> service.delete(id));

        verify(patientRepository, times(1)).delete(patient);
    }
}