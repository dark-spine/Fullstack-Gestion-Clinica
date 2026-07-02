package com.clinica.cancelaciones.controller;

import com.clinica.cancelaciones.dto.CancellationRequestDTO;
import com.clinica.cancelaciones.dto.CancellationResponseDTO;
import com.clinica.cancelaciones.service.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cancellations")
@RequiredArgsConstructor
public class CancellationController {
    private final CancellationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CancellationResponseDTO cancel(@RequestBody CancellationRequestDTO dto) {
        return service.processCancellation(dto);
    }
}
