package com.clinica.citas.controller;

import com.clinica.citas.dto.AppointmentRequestDTO;
import com.clinica.citas.dto.AppointmentResponseDTO;
import com.clinica.citas.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponseDTO create(@RequestBody AppointmentRequestDTO dto) {
        return service.createAppointment(dto);
    }

    @PostMapping("/{id}/cancel")
    public AppointmentResponseDTO cancel(@PathVariable Long id) {
        return service.cancelAppointment(id);
    }

    @GetMapping
    public List<AppointmentResponseDTO> getAll() {
        return service.listAppointments();
    }

    @GetMapping("/count")
    public Long count() {
        return service.countAppointments();
    }

    @GetMapping("/no-show-rate")
    public Double noShowRate() {
        return service.noShowRate();
    }
}
