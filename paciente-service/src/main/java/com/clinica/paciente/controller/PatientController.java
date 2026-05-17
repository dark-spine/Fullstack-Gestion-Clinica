package com.clinica.paciente.controller;

import com.clinica.paciente.dto.PatientRequestDTO;
import com.clinica.paciente.dto.PatientResponseDTO;
import com.clinica.paciente.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponseDTO create(@RequestBody PatientRequestDTO dto) {
        return service.createPatient(dto);
    }

    @GetMapping
    public List<PatientResponseDTO> list() {
        return service.listPatients();
    }
}
