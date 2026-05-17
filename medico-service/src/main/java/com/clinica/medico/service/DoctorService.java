package com.clinica.medico.service;

import com.clinica.medico.dto.DoctorRequestDTO;
import com.clinica.medico.dto.DoctorResponseDTO;
import com.clinica.medico.mapper.DoctorMapper;
import com.clinica.medico.model.Doctor;
import com.clinica.medico.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository repository;
    private final DoctorMapper mapper;

    @Transactional
    public DoctorResponseDTO createDoctor(DoctorRequestDTO dto) {
        Doctor saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public List<DoctorResponseDTO> listDoctors() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public Long countDoctors() {
        return repository.count();
    }
}
