package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.controller.docs.SpecialtyControllerDocs;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyRequestDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyResponseDTO;
import com.devsolutions.medsys.service.DoctorService;
import com.devsolutions.medsys.service.SpecialtyService;
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

    private final SpecialtyService specialtyService;
    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<SpecialtyResponseDTO> create(@Valid @RequestBody SpecialtyRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specialtyService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<SpecialtyResponseDTO>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(specialtyService.findAll(name));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<SpecialtyResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(specialtyService.findById(id));
    }

    @GetMapping("/{id}/doctors")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<DoctorResponseDTO>> findDoctorsBySpecialty(@PathVariable UUID id) {
        specialtyService.findById(id);
        return ResponseEntity.ok(doctorService.findBySpecialty(id));
    }
}
