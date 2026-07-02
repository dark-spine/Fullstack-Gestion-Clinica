package com.clinica.dashboard.controller;

import com.clinica.dashboard.dto.CitaDTO;
import com.clinica.dashboard.dto.MedicoDTO;
import com.clinica.dashboard.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) { this.service = service; }

    @GetMapping("/citas/hoy")
    public ResponseEntity<List<CitaDTO>> citasHoy() {
        return ResponseEntity.ok(service.citasHoy());
    }

    @GetMapping("/ingresos/totales")
    public ResponseEntity<Double> ingresosTotales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.ingresosTotales(inicio, fin));
    }

    @GetMapping("/medicos/activos")
    public ResponseEntity<List<MedicoDTO>> medicosActivos() {
        return ResponseEntity.ok(service.medicosActivos());
    }
}