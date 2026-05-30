package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.clinic.ClinicRequestDTO;
import com.devsolutions.medsys.dto.clinic.ClinicResponseDTO;
import com.devsolutions.medsys.dto.clinic.ClinicUpdateDTO;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.model.Clinic;
import com.devsolutions.medsys.repository.ClinicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    @InjectMocks
    private ClinicService clinicService;

    @Mock
    private ClinicRepository repository;

    // ---------------- CREATE ----------------

    @Test
    void deveCriarClinicaComSucesso() {

        ClinicRequestDTO dto = mock(ClinicRequestDTO.class);

        when(dto.name()).thenReturn("Clinica Teste");
        when(dto.cnpj()).thenReturn("123");
        when(dto.phone()).thenReturn("999");
        when(dto.address()).thenReturn("Rua A");
        when(dto.email()).thenReturn("clinic@email.com");

        when(repository.findByCnpj("123")).thenReturn(Optional.empty());

        Clinic saved = Clinic.builder()
                .id(UUID.randomUUID())
                .name("Clinica Teste")
                .cnpj("123")
                .build();

        when(repository.saveAndFlush(any())).thenReturn(saved);

        ClinicResponseDTO result = clinicService.create(dto);

        assertNotNull(result);
        assertEquals("Clinica Teste", result.name());
    }

    @Test
    void deveLancarErroQuandoCnpjJaExiste() {

        ClinicRequestDTO dto = mock(ClinicRequestDTO.class);
        when(dto.cnpj()).thenReturn("123");

        when(repository.findByCnpj("123"))
                .thenReturn(Optional.of(new Clinic()));

        assertThrows(BusinessException.class,
                () -> clinicService.create(dto));
    }

    // ---------------- FIND BY ID ----------------

    @Test
    void deveBuscarClinicaPorId() {

        UUID id = UUID.randomUUID();

        Clinic clinic = Clinic.builder()
                .id(id)
                .name("Clinica")
                .cnpj("123")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(clinic));

        ClinicResponseDTO result = clinicService.findById(id);

        assertNotNull(result);
        assertEquals("Clinica", result.name());
    }

    @Test
    void deveLancarErroSeClinicaNaoExistir() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clinicService.findById(id));
    }

    // ---------------- FIND ALL ----------------

    @Test
    void deveListarClinicas() {

        Clinic clinic = Clinic.builder()
                .id(UUID.randomUUID())
                .name("Clinica")
                .build();

        Page<Clinic> page = new PageImpl<>(List.of(clinic));

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<ClinicResponseDTO> result =
                clinicService.findAll(null, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    // ---------------- UPDATE ----------------

    @Test
    void deveAtualizarClinica() {

        UUID id = UUID.randomUUID();

        Clinic clinic = Clinic.builder()
                .id(id)
                .name("Antigo")
                .build();

        ClinicUpdateDTO dto = mock(ClinicUpdateDTO.class);
        when(dto.name()).thenReturn("Novo Nome");

        when(repository.findById(id)).thenReturn(Optional.of(clinic));
        when(repository.save(any())).thenReturn(clinic);

        ClinicResponseDTO result = clinicService.update(id, dto);

        assertEquals("Novo Nome", result.name());
    }

    // ---------------- DELETE ----------------

    @Test
    void deveDeletarClinica() {

        UUID id = UUID.randomUUID();

        Clinic clinic = Clinic.builder()
                .id(id)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(clinic));

        doNothing().when(repository).delete(clinic);

        assertDoesNotThrow(() -> clinicService.delete(id));

        verify(repository, times(1)).delete(clinic);
    }
}