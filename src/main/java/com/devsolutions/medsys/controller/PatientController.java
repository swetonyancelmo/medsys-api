package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.dto.patient.PatientRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    public ResponseEntity<PatientResponseDTO> create(@RequestBody @Valid PatientRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    public ResponseEntity<List<PatientResponseDTO>> findAll() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE', 'DOCTOR')")
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
