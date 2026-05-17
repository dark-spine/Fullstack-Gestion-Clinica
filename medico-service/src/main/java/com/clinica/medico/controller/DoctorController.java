package com.clinica.medico.controller;

import com.clinica.medico.dto.DoctorRequestDTO;
import com.clinica.medico.dto.DoctorResponseDTO;
import com.clinica.medico.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDTO createDoctor(@RequestBody DoctorRequestDTO dto) {
        return service.createDoctor(dto);
    }

    @GetMapping
    public List<DoctorResponseDTO> getDoctors() {
        return service.listDoctors();
    }

    @GetMapping("/count")
    public Long countDoctors() {
        return service.countDoctors();
    }
}
