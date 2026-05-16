package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityRequestDTO;
import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityResponseDTO;
import com.devsolutions.medsys.service.DoctorAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctor-availabilities")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService service;

    @PostMapping
    public ResponseEntity<DoctorAvailabilityResponseDTO> registerAvailability(
            @RequestBody @Valid DoctorAvailabilityRequestDTO dto) {
        DoctorAvailabilityResponseDTO response = service.registerAvailability(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAvailabilityResponseDTO>> findActiveAvailabilitiesByDoctor(
            @PathVariable UUID doctorId) {
        List<DoctorAvailabilityResponseDTO> response = service.findActiveAvailabilitiesByDoctor(doctorId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableAvailability(@PathVariable UUID id) {
        service.disableAvailability(id);
        return ResponseEntity.noContent().build();
    }

}
