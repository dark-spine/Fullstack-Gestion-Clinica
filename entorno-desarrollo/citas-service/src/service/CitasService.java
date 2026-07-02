package com.clinica.citas.service;

import com.clinica.citas.client.*;
import com.clinica.citas.dto.*;
import com.clinica.citas.model.Cita;
import com.clinica.citas.repository.CitaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService {

    private static final Logger log = LoggerFactory.getLogger(CitaService.class);

    private final CitaRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final AgendaClient agendaClient;

    public CitaService(CitaRepository repository,
                       PacienteClient pacienteClient,
                       MedicoClient medicoClient,
                       AgendaClient agendaClient) {
        this.repository = repository;
        this.pacienteClient = pacienteClient;
        this.medicoClient = medicoClient;
        this.agendaClient = agendaClient;
    }

    private CitaDTO toDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setPacienteId(cita.getPacienteId());
        dto.setMedicoId(cita.getMedicoId());
        dto.setSlotAgendaId(cita.getSlotAgendaId());
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setEstado(cita.getEstado());
        dto.setCreatedAt(cita.getCreatedAt());
        return dto;
    }


    public CitaDTO crearCita(CitaCreateDTO request) {
        log.info("Creando cita: pacienteId={}, medicoId={}, slotId={}",
                request.getPacienteId(), request.getMedicoId(), request.getSlotAgendaId());

        PacienteDTO paciente;
        try {
            paciente = pacienteClient.getPacienteById(request.getPacienteId());
            log.info("Paciente válido: {}", paciente.getNombres());
        } catch (FeignException.NotFound e) {
            log.warn("Paciente id={} no existe", request.getPacienteId());
            throw new RuntimeException("Paciente no encontrado");
        } catch (FeignException e) {
            log.error("Error al consultar paciente-service: {}", e.getMessage());
            throw new RuntimeException("Servicio de pacientes no disponible");
        }

        MedicoDTO medico;
        try {
            medico = medicoClient.getMedicoById(request.getMedicoId());
            log.info("Médico válido: {}", medico.getNombre());
        } catch (FeignException.NotFound e) {
            log.warn("Médico id={} no existe", request.getMedicoId());
            throw new RuntimeException("Médico no encontrado");
        } catch (FeignException e) {
            log.error("Error al consultar medico-service: {}", e.getMessage());
            throw new RuntimeException("Servicio de médicos no disponible");
        }

        SlotAgendaDTO slot;
        try {
            slot = agendaClient.getSlotById(request.getSlotAgendaId());
            if (!"DISPONIBLE".equals(slot.getEstado())) {
                throw new RuntimeException("Slot no disponible");
            }
            slot = agendaClient.reservarSlot(slot.getId());
            log.info("Slot reservado correctamente");
        } catch (FeignException.NotFound e) {
            log.warn("Slot id={} no existe", request.getSlotAgendaId());
            throw new RuntimeException("Slot no encontrado");
        } catch (FeignException e) {
            log.error("Error al consultar agenda-service: {}", e.getMessage());
            throw new RuntimeException("Servicio de agenda no disponible");
        }

        Cita cita = new Cita();
        cita.setPacienteId(paciente.getId());
        cita.setMedicoId(medico.getId());
        cita.setSlotAgendaId(slot.getId());
        cita.setMotivoConsulta(request.getMotivoConsulta());
        cita.setEstado("CONFIRMADA");
        cita.setCreatedAt(LocalDateTime.now());

        Cita guardada = repository.save(cita);
        log.info("Cita creada con id={}", guardada.getId());

        return toDTO(guardada);
    }

    public List<CitaDTO> listarTodos() {
        log.info("Listando todas las citas");
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CitaDTO obtenerPorId(Long id) {
        log.info("Buscando cita con id={}", id);
        Cita cita = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return toDTO(cita);
    }

    public List<CitaDTO> listarPorPaciente(Long pacienteId) {
        log.info("Listando citas del paciente id={}", pacienteId);
        return repository.findByPacienteId(pacienteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CitaDTO> listarPorMedico(Long medicoId) {
        log.info("Listando citas del médico id={}", medicoId);
        return repository.findByMedicoId(medicoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CitaDTO actualizarCita(Long id, CitaCreateDTO request) {
        log.info("Actualizando cita id={}", id);

        Cita cita = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!cita.getPacienteId().equals(request.getPacienteId())) {
            try {
                pacienteClient.getPacienteById(request.getPacienteId());
            } catch (FeignException e) {
                throw new RuntimeException("Paciente no válido");
            }
        }
        if (!cita.getMedicoId().equals(request.getMedicoId())) {
            try {
                medicoClient.getMedicoById(request.getMedicoId());
            } catch (FeignException e) {
                throw new RuntimeException("Médico no válido");
            }
        }
        if (!cita.getSlotAgendaId().equals(request.getSlotAgendaId())) {
            try {
                SlotAgendaDTO slot = agendaClient.getSlotById(request.getSlotAgendaId());
                if (!"DISPONIBLE".equals(slot.getEstado())) {
                    throw new RuntimeException("El nuevo slot no está disponible");
                }
            } catch (FeignException e) {
                throw new RuntimeException("Slot no válido");
            }
        }

        cita.setPacienteId(request.getPacienteId());
        cita.setMedicoId(request.getMedicoId());
        cita.setSlotAgendaId(request.getSlotAgendaId());
        cita.setMotivoConsulta(request.getMotivoConsulta());

        Cita actualizada = repository.save(cita);
        log.info("Cita id={} actualizada", actualizada.getId());

        return toDTO(actualizada);
    }

    public CitaDTO cambiarEstado(Long id, String estado) {
        log.info("Cambiando estado de cita id={} a {}", id, estado);

        Cita cita = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstado(estado);
        Cita actualizada = repository.save(cita);
        log.info("Estado de cita id={} actualizado", id);

        return toDTO(actualizada);
    }

    public void eliminarCita(Long id) {
        log.info("Eliminando cita id={}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada");
        }
        repository.deleteById(id);
        log.info("Cita id={} eliminada", id);
    }
}