package com.clinica.notificaciones.service;

import com.clinica.notificaciones.dto.NotificacionCreateDTO;
import com.clinica.notificaciones.dto.NotificacionDTO;
import com.clinica.notificaciones.dto.RecordatorioDTO;
import com.clinica.notificaciones.model.Notificacion;
import com.clinica.notificaciones.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {
    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);
    private final NotificacionRepository repository;

    public NotificacionService(NotificacionRepository repository) {
        this.repository = repository;
    }

    private NotificacionDTO toDTO(Notificacion n) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(n.getId());
        dto.setDestinatarioId(n.getDestinatarioId());
        dto.setTipo(n.getTipo());
        dto.setAsunto(n.getAsunto());
        dto.setMensaje(n.getMensaje());
        dto.setEstado(n.getEstado());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }

    public NotificacionDTO crear(NotificacionCreateDTO request) {
        log.info("Creando notificación para destinatarioId={}", request.getDestinatarioId());
        Notificacion n = new Notificacion();
        n.setDestinatarioId(request.getDestinatarioId());
        n.setTipo(request.getTipo());
        n.setAsunto(request.getAsunto());
        n.setMensaje(request.getMensaje());
        n.setEstado("PENDIENTE");
        n.setCreatedAt(LocalDateTime.now());
        Notificacion guardada = repository.save(n);
        return toDTO(guardada);
    }

    public void enviarRecordatorio(RecordatorioDTO recordatorio) {
        log.info("Enviando recordatorio para citaId={}", recordatorio.getCitaId());
        Notificacion n = new Notificacion();
        n.setDestinatarioId(recordatorio.getDestinatarioId());
        n.setTipo("EMAIL");
        n.setAsunto("Recordatorio de cita médica");
        n.setMensaje("Tiene una cita con el Dr. " + recordatorio.getMedicoNombre() + " para el " + recordatorio.getFechaCita());
        n.setEstado("ENVIADO");
        n.setCreatedAt(LocalDateTime.now());
        repository.save(n);
    }

    public List<NotificacionDTO> listarPorDestinatario(Long destinatarioId) {
        return repository.findByDestinatarioId(destinatarioId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<NotificacionDTO> listarPendientes() {
        return repository.findByEstado("PENDIENTE").stream().map(this::toDTO).collect(Collectors.toList());
    }
}