package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.controller.docs.PrescriptionControllerDocs;
import com.devsolutions.medsys.dto.prescription.PrescriptionRequestDTO;
import com.devsolutions.medsys.dto.prescription.PrescriptionResponseDTO;
import com.devsolutions.medsys.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController implements PrescriptionControllerDocs {

    private final PrescriptionService service;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionResponseDTO> create(@RequestBody @Valid PrescriptionRequestDTO dto) {
        PrescriptionResponseDTO response = service.createPrescription(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<PrescriptionResponseDTO> findById(@PathVariable UUID id) {
        PrescriptionResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<PrescriptionResponseDTO> findByAppointmentId(@PathVariable UUID appointmentId) {
        PrescriptionResponseDTO response = service.findByAppointmentId(appointmentId);
        return ResponseEntity.ok(response);
    }
}
