package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.controller.docs.SpecialtyControllerDocs;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyRequestDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyResponseDTO;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.model.Specialty;
import com.devsolutions.medsys.repository.SpecialtyRepository;
import com.devsolutions.medsys.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController implements SpecialtyControllerDocs {

    private final SpecialtyRepository specialtyRepository;
    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<SpecialtyResponseDTO> create(@Valid @RequestBody SpecialtyRequestDTO dto) {
        if (specialtyRepository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessException("Especialidade já cadastrada com esse nome: " + dto.name());
        }

        Specialty specialty = Specialty.builder()
                .name(dto.name())
                .description(dto.description())
                .build();

        Specialty saved = specialtyRepository.save(specialty);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SpecialtyResponseDTO(saved.getId(), saved.getName(), saved.getDescription()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<SpecialtyResponseDTO>> findAll(@RequestParam(required = false) String name) {
        List<Specialty> specialties = (name != null && !name.isBlank())
                ? specialtyRepository.findByNameContainingIgnoreCase(name)
                : specialtyRepository.findAll();

        List<SpecialtyResponseDTO> result = specialties.stream()
                .map(s -> new SpecialtyResponseDTO(s.getId(), s.getName(), s.getDescription()))
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<SpecialtyResponseDTO> findById(@PathVariable UUID id) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada."));
        return ResponseEntity.ok(new SpecialtyResponseDTO(specialty.getId(), specialty.getName(), specialty.getDescription()));
    }

    @GetMapping("/{id}/doctors")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<DoctorResponseDTO>> findDoctorsBySpecialty(@PathVariable UUID id) {
        specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada."));
        return ResponseEntity.ok(doctorService.findBySpecialty(id));
    }
}
