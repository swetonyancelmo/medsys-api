package com.devsolutions.medsys.service;

import com.devsolutions.medsys.repository.DisponibilidadeMedicoRepository;
import com.devsolutions.medsys.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisponibilidadeMedico {

    private final DisponibilidadeMedicoRepository repository;
    private final MedicoRepository medicoRepository;
}
