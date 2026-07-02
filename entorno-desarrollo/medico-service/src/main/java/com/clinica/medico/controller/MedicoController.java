package com.clinica.medico.controller;

import com.clinica.medico.dto.MedicoCreateDTO;
import com.clinica.medico.dto.MedicoDTO;
import com.clinica.medico.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> crear(@Valid @RequestBody MedicoCreateDTO request) {
        MedicoDTO nuevo = service.crearMedico(request);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<MedicoDTO>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<List<MedicoDTO>> listarPorEspecialidad(@PathVariable Long especialidadId) {
        return ResponseEntity.ok(service.listarPorEspecialidad(especialidadId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MedicoCreateDTO request) {
        return ResponseEntity.ok(service.actualizarMedico(id, request));
    }

    @PatchMapping("/{id}/activo")
    public ResponseEntity<Void> activarDesactivar(@PathVariable Long id, @RequestParam Boolean activo) {
        service.activarDesactivar(id, activo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarMedico(id);
        return ResponseEntity.noContent().build();
    }
}