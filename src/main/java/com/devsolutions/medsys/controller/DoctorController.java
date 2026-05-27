package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.controller.docs.DoctorControllerDocs;
import com.devsolutions.medsys.dto.doctor.DoctorRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.doctor.DoctorUpdateDTO;
import com.devsolutions.medsys.service.DoctorService;
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
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Validated
public class DoctorController implements DoctorControllerDocs {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<DoctorResponseDTO> create(@RequestBody @Valid DoctorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR')")
    public ResponseEntity<Page<DoctorResponseDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "12") @Positive Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(doctorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<DoctorResponseDTO> update(@PathVariable UUID id,
                                                    @RequestBody DoctorUpdateDTO dto) {
        return ResponseEntity.ok(doctorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
