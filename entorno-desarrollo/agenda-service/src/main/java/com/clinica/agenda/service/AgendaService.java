package com.clinica.agenda.service;

import com.clinica.agenda.dto.SlotAgendaCreateDTO;
import com.clinica.agenda.dto.SlotAgendaDTO;
import com.clinica.agenda.model.SlotAgenda;
import com.clinica.agenda.repository.AgendaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendaService {

    private static final Logger log = LoggerFactory.getLogger(AgendaService.class);

    private final AgendaRepository repository;

    public AgendaService(AgendaRepository repository) {
        this.repository = repository;
    }

    private SlotAgendaDTO toDTO(SlotAgenda slot) {
        SlotAgendaDTO dto = new SlotAgendaDTO();
        dto.setId(slot.getId());
        dto.setMedicoId(slot.getMedicoId());
        dto.setFecha(slot.getFecha());
        dto.setHoraInicio(slot.getHoraInicio());
        dto.setHoraFin(slot.getHoraFin());
        dto.setEstado(slot.getEstado());
        return dto;
    }

    private SlotAgenda toEntity(SlotAgendaCreateDTO dto) {
        SlotAgenda slot = new SlotAgenda();
        slot.setMedicoId(dto.getMedicoId());
        slot.setFecha(dto.getFecha());
        slot.setHoraInicio(dto.getHoraInicio());
        slot.setHoraFin(dto.getHoraFin());
        return slot;
    }


    public SlotAgendaDTO crearSlot(SlotAgendaCreateDTO request) {
        log.info("Creando slot para médicoId={}, fecha={}", request.getMedicoId(), request.getFecha());

        SlotAgenda slot = toEntity(request);
        if (slot.getEstado() == null) {
            slot.setEstado("DISPONIBLE");
        }

        SlotAgenda guardado = repository.save(slot);
        log.info("Slot creado con id={}", guardado.getId());

        return toDTO(guardado);
    }

    public List<SlotAgendaDTO> listarTodos() {
        log.info("Listando todos los slots");
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SlotAgendaDTO obtenerPorId(Long id) {
        log.info("Buscando slot con id={}", id);
        SlotAgenda slot = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot no encontrado"));
        return toDTO(slot);
    }

    public List<SlotAgendaDTO> listarPorMedico(Long medicoId) {
        log.info("Listando slots del médico id={}", medicoId);
        return repository.findByMedicoIdAndFecha(medicoId, null).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SlotAgendaDTO> listarDisponiblesPorMedico(Long medicoId) {
        log.info("Listando slots disponibles del médico id={}", medicoId);
        return repository.findByMedicoIdAndEstado(medicoId, "DISPONIBLE").stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SlotAgendaDTO actualizarSlot(Long id, SlotAgendaCreateDTO request) {
        log.info("Actualizando slot id={}", id);
        SlotAgenda existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot no encontrado"));

        existente.setFecha(request.getFecha());
        existente.setHoraInicio(request.getHoraInicio());
        existente.setHoraFin(request.getHoraFin());

        SlotAgenda actualizado = repository.save(existente);
        log.info("Slot id={} actualizado", id);

        return toDTO(actualizado);
    }

    public SlotAgendaDTO cambiarEstado(Long id, String estado) {
        log.info("Cambiando estado del slot id={} a {}", id, estado);
        SlotAgenda slot = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot no encontrado"));

        slot.setEstado(estado);
        SlotAgenda actualizado = repository.save(slot);
        log.info("Estado del slot id={} actualizado", id);

        return toDTO(actualizado);
    }

    public void eliminarSlot(Long id) {
        log.info("Eliminando slot id={}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Slot no encontrado");
        }
        repository.deleteById(id);
        log.info("Slot id={} eliminado", id);
    }
}