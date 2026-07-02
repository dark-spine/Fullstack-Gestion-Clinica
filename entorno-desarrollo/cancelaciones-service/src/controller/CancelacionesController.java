package com.clinica.cancelaciones.controller;

import com.clinica.cancelaciones.dto.CancelacionCreateDTO;
import com.clinica.cancelaciones.dto.CancelacionDTO;
import com.clinica.cancelaciones.service.CancelacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cancelaciones")
public class CancelacionController {

    private final CancelacionService service;

    public CancelacionController(CancelacionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CancelacionDTO> crear(@Valid @RequestBody CancelacionCreateDTO request) {
        CancelacionDTO nueva = service.registrarCancelacion(request);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CancelacionDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CancelacionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<List<CancelacionDTO>> listarPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(service.listarPorCita(citaId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CancelacionDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CancelacionDTO>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.listarPorEstado(estado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CancelacionDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            @RequestParam(required = false) String motivo) {
        return ResponseEntity.ok(service.cambiarEstado(id, nuevoEstado, motivo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarCancelacion(id);
        return ResponseEntity.noContent().build();
    }
}