package com.clinica.notificaciones.controller;

import com.clinica.notificaciones.dto.NotificacionCreateDTO;
import com.clinica.notificaciones.dto.NotificacionDTO;
import com.clinica.notificaciones.dto.RecordatorioDTO;
import com.clinica.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    private final NotificacionService service;

    public NotificacionController(NotificacionService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<NotificacionDTO> crear(@Valid @RequestBody NotificacionCreateDTO request) {
        return new ResponseEntity<>(service.crear(request), HttpStatus.CREATED);
    }

    @PostMapping("/recordatorio")
    public ResponseEntity<Void> enviarRecordatorio(@RequestBody RecordatorioDTO recordatorio) {
        service.enviarRecordatorio(recordatorio);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/destinatario/{destinatarioId}")
    public ResponseEntity<List<NotificacionDTO>> listarPorDestinatario(@PathVariable Long destinatarioId) {
        return ResponseEntity.ok(service.listarPorDestinatario(destinatarioId));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<NotificacionDTO>> listarPendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }
}