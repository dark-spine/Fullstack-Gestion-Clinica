package com.clinica.cancelaciones.service;

import com.clinica.cancelaciones.client.CitasClient;
import com.clinica.cancelaciones.dto.CancelacionCreateDTO;
import com.clinica.cancelaciones.dto.CancelacionDTO;
import com.clinica.cancelaciones.dto.CitaDTO;
import com.clinica.cancelaciones.model.Cancelacion;
import com.clinica.cancelaciones.repository.CancelacionRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CancelacionService {

    private static final Logger log = LoggerFactory.getLogger(CancelacionService.class);

    private final CancelacionRepository repository;
    private final CitasClient citasClient;

    public CancelacionService(CancelacionRepository repository, CitasClient citasClient) {
        this.repository = repository;
        this.citasClient = citasClient;
    }

    private CancelacionDTO toDTO(Cancelacion cancelacion) {
        CancelacionDTO dto = new CancelacionDTO();
        dto.setId(cancelacion.getId());
        dto.setCitaId(cancelacion.getCitaId());
        dto.setPacienteId(cancelacion.getPacienteId());
        dto.setMotivo(cancelacion.getMotivo());
        dto.setEstado(cancelacion.getEstado());
        dto.setFechaSolicitud(cancelacion.getFechaSolicitud());
        dto.setMotivoRespuesta(cancelacion.getMotivoRespuesta());
        return dto;
    }

    private Cancelacion toEntity(CancelacionCreateDTO dto) {
        Cancelacion cancelacion = new Cancelacion();
        cancelacion.setCitaId(dto.getCitaId());
        cancelacion.setPacienteId(dto.getPacienteId());
        cancelacion.setMotivo(dto.getMotivo());
        return cancelacion;
    }

    public CancelacionDTO registrarCancelacion(CancelacionCreateDTO request) {
        log.info("Registrando cancelación para citaId={}, pacienteId={}", request.getCitaId(), request.getPacienteId());

        CitaDTO cita;
        try {
            cita = citasClient.obtenerCitaPorId(request.getCitaId());
            log.info("Cita encontrada: id={}, estado={}", cita.getId(), cita.getEstado());
        } catch (FeignException.NotFound e) {
            log.warn("Cita id={} no existe", request.getCitaId());
            throw new RuntimeException("La cita no existe");
        } catch (FeignException e) {
            log.error("Error al consultar citas-service: {}", e.getMessage());
            throw new RuntimeException("Servicio de citas no disponible");
        }

        if (!cita.getPacienteId().equals(request.getPacienteId())) {
            log.warn("El paciente id={} no es el titular de la cita id={}", request.getPacienteId(), request.getCitaId());
            throw new RuntimeException("La cancelación solo puede ser realizada por el paciente titular de la cita");
        }

        if ("CANCELADA".equals(cita.getEstado())) {
            throw new RuntimeException("La cita ya se encuentra cancelada");
        }

        Cancelacion cancelacion = toEntity(request);
        cancelacion.setEstado("PENDIENTE");
        cancelacion.setFechaSolicitud(LocalDateTime.now());

        Cancelacion guardada = repository.save(cancelacion);
        log.info("Solicitud de cancelación registrada con id={}", guardada.getId());

        return toDTO(guardada);
    }

    public List<CancelacionDTO> listarTodas() {
        log.info("Listando todas las solicitudes de cancelación");
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CancelacionDTO obtenerPorId(Long id) {
        log.info("Buscando cancelación con id={}", id);
        Cancelacion cancelacion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancelación no encontrada"));
        return toDTO(cancelacion);
    }

    public List<CancelacionDTO> listarPorCita(Long citaId) {
        log.info("Listando cancelaciones de la cita id={}", citaId);
        return repository.findByCitaId(citaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CancelacionDTO> listarPorPaciente(Long pacienteId) {
        log.info("Listando cancelaciones del paciente id={}", pacienteId);
        return repository.findByPacienteId(pacienteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CancelacionDTO> listarPorEstado(String estado) {
        log.info("Listando cancelaciones con estado={}", estado);
        return repository.findByEstado(estado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CancelacionDTO cambiarEstado(Long id, String nuevoEstado, String motivoRespuesta) {
        log.info("Cambiando estado de cancelación id={} a {}", id, nuevoEstado);

        Cancelacion cancelacion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancelación no encontrada"));

        cancelacion.setEstado(nuevoEstado);
        if (motivoRespuesta != null && !motivoRespuesta.isEmpty()) {
            cancelacion.setMotivoRespuesta(motivoRespuesta);
        }

        if ("APROBADA".equals(nuevoEstado)) {
            try {
                citasClient.cambiarEstadoCita(cancelacion.getCitaId(), "CANCELADA");
                log.info("Estado de cita id={} actualizado a CANCELADA", cancelacion.getCitaId());
            } catch (FeignException e) {
                log.error("Error al actualizar estado de la cita: {}", e.getMessage());
                throw new RuntimeException("No se pudo actualizar el estado de la cita");
            }
        }

        Cancelacion actualizada = repository.save(cancelacion);
        log.info("Estado de cancelación id={} actualizado", id);

        return toDTO(actualizada);
    }

    public void eliminarCancelacion(Long id) {
        log.info("Eliminando cancelación id={}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cancelación no encontrada");
        }
        repository.deleteById(id);
        log.info("Cancelación id={} eliminada", id);
    }
}