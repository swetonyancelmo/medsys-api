package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.auth.DoctorRegisterRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.doctor.DoctorUpdateDTO;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.DoctorMapper;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.Specialty;
import com.devsolutions.medsys.model.User;
import com.devsolutions.medsys.repository.DoctorRepository;
import com.devsolutions.medsys.repository.SpecialtyRepository;
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
class DoctorServiceTest {

    @InjectMocks
    private DoctorService service;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private DoctorMapper doctorMapper;

    // ---------------- CREATE ----------------

    @Test
    void deveCriarMedicoComSucesso() {

        UUID userId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();

        User user = User.builder().id(userId).build();
        Specialty specialty = Specialty.builder().id(specialtyId).build();

        DoctorRequestDTO dto = mock(DoctorRequestDTO.class);

        when(dto.userId()).thenReturn(userId);
        when(dto.specialtyId()).thenReturn(specialtyId);
        when(dto.name()).thenReturn("Dr. Teste");
        when(dto.crm()).thenReturn("12345");
        when(dto.phone()).thenReturn("999999");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(specialty));

        Doctor doctorSaved = Doctor.builder()
                .id(UUID.randomUUID())
                .user(user)
                .specialty(specialty)
                .build();

        when(doctorRepository.saveAndFlush(any())).thenReturn(doctorSaved);

        DoctorResponseDTO response = mock(DoctorResponseDTO.class);
        when(doctorMapper.toDTO(doctorSaved)).thenReturn(response);

        DoctorResponseDTO result = service.create(dto);

        assertNotNull(result);
        verify(doctorRepository, times(1)).saveAndFlush(any());
    }

    // ---------------- CREATE FROM USER ----------------

    @Test
    void deveCriarMedicoFromUser() {

        User user = User.builder().id(UUID.randomUUID()).build();
        UUID specialtyId = UUID.randomUUID();

        DoctorRegisterRequestDTO dto = mock(DoctorRegisterRequestDTO.class);

        when(dto.specialtyId()).thenReturn(specialtyId);
        when(dto.name()).thenReturn("Dr. Teste");
        when(dto.crm()).thenReturn("12345");
        when(dto.phone()).thenReturn("999");

        Specialty specialty = Specialty.builder().id(specialtyId).build();

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(specialty));

        Doctor doctorSaved = Doctor.builder().id(UUID.randomUUID()).build();

        when(doctorRepository.saveAndFlush(any())).thenReturn(doctorSaved);

        DoctorResponseDTO response = mock(DoctorResponseDTO.class);
        when(doctorMapper.toDTO(doctorSaved)).thenReturn(response);

        DoctorResponseDTO result = service.createFromUser(user, dto);

        assertNotNull(result);
    }

    // ---------------- FIND BY ID ----------------

    @Test
    void deveBuscarMedicoPorId() {

        UUID id = UUID.randomUUID();

        Doctor doctor = Doctor.builder().id(id).build();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));

        DoctorResponseDTO response = mock(DoctorResponseDTO.class);
        when(doctorMapper.toDTO(doctor)).thenReturn(response);

        DoctorResponseDTO result = service.findById(id);

        assertNotNull(result);
    }

    @Test
    void deveLancarErroSeMedicoNaoExiste() {

        UUID id = UUID.randomUUID();

        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findById(id));
    }

    // ---------------- FIND ALL ----------------

    @Test
    void deveListarMedicos() {

        Doctor doctor = Doctor.builder().id(UUID.randomUUID()).build();

        when(doctorRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(doctor)));

        when(doctorMapper.toDTO(doctor))
                .thenReturn(mock(DoctorResponseDTO.class));

        Page<DoctorResponseDTO> result = service.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    // ---------------- DELETE ----------------

    @Test
    void deveDeletarMedico() {

        UUID id = UUID.randomUUID();

        Doctor doctor = Doctor.builder().id(id).build();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));

        assertDoesNotThrow(() -> service.delete(id));

        verify(doctorRepository, times(1)).delete(doctor);
    }
}