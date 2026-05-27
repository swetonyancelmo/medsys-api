package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.controller.docs.AppointmentControllerDocs;
import com.devsolutions.medsys.dto.appointment.AppointmentRequestDTO;
import com.devsolutions.medsys.dto.appointment.AppointmentResponseDTO;
import com.devsolutions.medsys.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController implements AppointmentControllerDocs {

    private final AppointmentService service;

    @PostMapping()
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<AppointmentResponseDTO> schedule(@RequestBody @Valid AppointmentRequestDTO dto){
        AppointmentResponseDTO response = service.schedule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id) {
        AppointmentResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{id}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR')")
    public ResponseEntity<List<AppointmentResponseDTO>> findByDoctorAndDateRange(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AppointmentResponseDTO> response = service.findByDoctorAndDateRange(id, start, end);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findByPatient/{id}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<AppointmentResponseDTO>> findByPatient(@PathVariable UUID id) {
        List<AppointmentResponseDTO> response = service.findByPatient(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/range")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<List<AppointmentResponseDTO>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AppointmentResponseDTO> response = service.findByDateRange(start, end);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<AppointmentResponseDTO> cancel(@PathVariable UUID id){
        AppointmentResponseDTO response = service.cancel(id);
        return ResponseEntity.ok(response);
    }
}
