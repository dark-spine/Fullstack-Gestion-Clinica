package com.clinica.agenda.controller;

import com.clinica.agenda.dto.SlotRequestDTO;
import com.clinica.agenda.dto.SlotResponseDTO;
import com.clinica.agenda.service.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@RequiredArgsConstructor
public class AgendaController {
    private final AgendaService service;

    @PostMapping("/slots")
    @ResponseStatus(HttpStatus.CREATED)
    public SlotResponseDTO createSlot(@RequestBody SlotRequestDTO dto) {
        return service.createSlot(dto);
    }

    @PostMapping("/slots/reserve")
    public SlotResponseDTO reserveSlot(@RequestParam Long doctorId, @RequestParam String startTime) {
        return service.reserveSlot(doctorId, LocalDateTime.parse(startTime));
    }

    @GetMapping("/slots/available")
    public List<SlotResponseDTO> getAvailableSlots(@RequestParam Long doctorId) {
        return service.findAvailable(doctorId);
    }
}
