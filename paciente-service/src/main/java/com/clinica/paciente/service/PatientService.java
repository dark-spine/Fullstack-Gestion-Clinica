package com.clinica.paciente.service;

import com.clinica.paciente.dto.PatientRequestDTO;
import com.clinica.paciente.dto.PatientResponseDTO;
import com.clinica.paciente.mapper.PatientMapper;
import com.clinica.paciente.model.Patient;
import com.clinica.paciente.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;
    private final PatientMapper mapper;

    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO dto) {
        Patient saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public List<PatientResponseDTO> listPatients() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}
