package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.dto.appointment.AppointmentRequestDTO;
import com.devsolutions.medsys.dto.appointment.AppointmentResponseDTO;
import com.devsolutions.medsys.model.Appointment;
import com.devsolutions.medsys.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping()
    public ResponseEntity<AppointmentResponseDTO> schedule(@RequestBody AppointmentRequestDTO dto){
        AppointmentResponseDTO response = service.schedule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id) {
        AppointmentResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/{start}/{end}")
    public ResponseEntity<List<AppointmentResponseDTO>> findByDoctorAndDateRange(@PathVariable UUID id,
                                                                @PathVariable LocalDateTime start,
                                                                @PathVariable LocalDateTime end) {
        List<AppointmentResponseDTO> response = service.findByDoctorAndDateRange(id, start, end);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findByPatient/{id}")
    public ResponseEntity<List<AppointmentResponseDTO>> findByPatient(@PathVariable UUID id) {
        List<AppointmentResponseDTO> response = service.findByPatient(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{start}/{end}")
    public ResponseEntity<List<AppointmentResponseDTO>> findByDateRange(@PathVariable LocalDateTime start,
                                                                        @PathVariable LocalDateTime end) {
        List<AppointmentResponseDTO> response = service.findByDateRange(start, end);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancel(@PathVariable UUID id){
        AppointmentResponseDTO response = service.cancel(id);
        return ResponseEntity.ok(response);
    }
}
