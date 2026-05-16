package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.model.Patient;
import com.devsolutions.medsys.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Criar paciente
    @PostMapping
    public Patient create(@RequestBody Patient patient) {

        return patientService.create(patient);
    }

    // Listar pacientes
    @GetMapping
    public List<Patient> findAll() {

        return patientService.findAll();
    }

    // Buscar paciente por ID
    @GetMapping("/{id}")
    public Patient findById(@PathVariable UUID id) {

        return patientService.findById(id);
    }

    // Deletar paciente
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {

        patientService.delete(id);
    }
}