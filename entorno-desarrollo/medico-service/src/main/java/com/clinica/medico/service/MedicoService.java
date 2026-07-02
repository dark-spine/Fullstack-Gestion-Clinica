package com.clinica.medico.service;

import com.clinica.medico.dto.MedicoCreateDTO;
import com.clinica.medico.dto.MedicoDTO;
import com.clinica.medico.model.Medico;
import com.clinica.medico.repository.MedicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.clinica.medico.exception.ResourceNotFoundException;
import com.clinica.medico.exception.DuplicateResourceException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    private static final Logger log = LoggerFactory.getLogger(MedicoService.class);

    private final MedicoRepository repository;

    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    private MedicoDTO toDTO(Medico medico) {
        MedicoDTO dto = new MedicoDTO();
        dto.setId(medico.getId());
        dto.setNombre(medico.getNombre());
        dto.setEmail(medico.getEmail());
        dto.setEspecialidadId(medico.getEspecialidadId());
        dto.setActivo(medico.getActivo());
        return dto;
    }

    private Medico toEntity(MedicoCreateDTO dto) {
        Medico medico = new Medico();
        medico.setNombre(dto.getNombre());
        medico.setEmail(dto.getEmail());
        medico.setEspecialidadId(dto.getEspecialidadId());
        medico.setActivo(true);  // Por defecto activo
        return medico;
    }


    public MedicoDTO crearMedico(MedicoCreateDTO request) {
        log.info("Creando médico: {}", request.getNombre());

        Medico medico = toEntity(request);
        Medico guardado = repository.save(medico);
            log.info("Médico creado exitosamente con id={}", guardado.getId());

        return toDTO(guardado);
    }

    public List<MedicoDTO> listarTodos() {
        log.info("Listando todos los médicos");
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MedicoDTO obtenerPorId(Long id) {
        log.info("Buscando médico con id={}", id);
        Medico medico = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        return toDTO(medico);
    }

    public List<MedicoDTO> listarActivos() {
        log.info("Listando médicos activos");
        return repository.findByActivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MedicoDTO> listarPorEspecialidad(Long especialidadId) {
        log.info("Listando médicos por especialidad id={}", especialidadId);
        return repository.findByEspecialidadId(especialidadId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MedicoDTO actualizarMedico(Long id, MedicoCreateDTO request) {
        log.info("Actualizando médico id={}", id);

        Medico existente = repository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Médico no encontrado con id={}", id);
                        return new ResourceNotFoundException("Médico no encontrado con id: " + id);
                    });

        existente.setNombre(request.getNombre());
        existente.setEmail(request.getEmail());
        existente.setEspecialidadId(request.getEspecialidadId());

        Medico actualizado = repository.save(existente);
            log.info("Médico id={} actualizado exitosamente", id);

        return toDTO(actualizado);
    }

    public void activarDesactivar(Long id, Boolean activo) {
        log.info("Cambiando estado activo del médico id={} a {}", id, activo);

            Medico medico = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Médico no encontrado con id={}", id);
                    return new ResourceNotFoundException("Médico no encontrado con id: " + id);
                });

        medico.setActivo(activo);
        repository.save(medico);
        log.info("Estado del médico id={} actualizado", id);
    }

    public void eliminarMedico(Long id) {
        log.info("Eliminando médico id={}", id);
        if (!repository.existsById(id)) {
              log.error("Médico no encontrado para eliminar, id={}", id);
              throw new ResourceNotFoundException("Médico no encontrado con id: " + id);
        }
        repository.deleteById(id);
            log.info("Médico id={} eliminado exitosamente", id);
    }
}