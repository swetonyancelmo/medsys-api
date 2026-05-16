package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.dto.prescription.PrescriptionRequestDTO;
import com.devsolutions.medsys.dto.prescription.PrescriptionResponseDTO;
import com.devsolutions.medsys.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService service;

    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> create(@RequestBody @Valid PrescriptionRequestDTO dto) {
        PrescriptionResponseDTO response = service.createPrescription(dto);
        // Retorna estritamente o status 201 com o payload gerado no corpo
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> findById(@PathVariable UUID id) {
        PrescriptionResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PrescriptionResponseDTO> findByAppointmentId(@PathVariable UUID appointmentId) {
        PrescriptionResponseDTO response = service.findByAppointmentId(appointmentId);
        return ResponseEntity.ok(response);
    }
}
