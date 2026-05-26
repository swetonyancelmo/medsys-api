package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.dto.doctor.DoctorRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
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
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> create(@RequestBody @Valid DoctorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE', 'DOCTOR')")
    public ResponseEntity<List<DoctorResponseDTO>> findAll() {
        return ResponseEntity.ok(doctorService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE', 'DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
