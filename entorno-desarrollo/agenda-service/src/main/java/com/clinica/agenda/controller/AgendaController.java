package com.clinica.agenda.controller;

import com.clinica.agenda.dto.SlotAgendaCreateDTO;
import com.clinica.agenda.dto.SlotAgendaDTO;
import com.clinica.agenda.service.AgendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    private final AgendaService service;

    public AgendaController(AgendaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SlotAgendaDTO> crear(@Valid @RequestBody SlotAgendaCreateDTO request) {
        SlotAgendaDTO nuevoSlot = service.crearSlot(request);
        return new ResponseEntity<>(nuevoSlot, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SlotAgendaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SlotAgendaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<SlotAgendaDTO>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(service.listarPorMedico(medicoId));
    }

    @GetMapping("/medico/{medicoId}/disponibles")
    public ResponseEntity<List<SlotAgendaDTO>> listarDisponibles(@PathVariable Long medicoId) {
        return ResponseEntity.ok(service.listarDisponiblesPorMedico(medicoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SlotAgendaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody SlotAgendaCreateDTO request) {
        return ResponseEntity.ok(service.actualizarSlot(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SlotAgendaDTO> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(service.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarSlot(id);
        return ResponseEntity.noContent().build();
    }
}