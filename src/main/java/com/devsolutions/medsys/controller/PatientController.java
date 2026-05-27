package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.controller.docs.PatientControllerDocs;
import com.devsolutions.medsys.dto.patient.PatientRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientUpdateDTO;
import com.devsolutions.medsys.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Validated
public class PatientController implements PatientControllerDocs {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<PatientResponseDTO> create(@RequestBody @Valid PatientRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<Page<PatientResponseDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "12") @Positive Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(patientService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<PatientResponseDTO> update(@PathVariable UUID id,
                                                     @RequestBody PatientUpdateDTO dto) {
        return ResponseEntity.ok(patientService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
