package com.clinica.paciente.service;

import com.clinica.paciente.dto.PacienteCreateDTO;
import com.clinica.paciente.dto.PacienteDTO;
import com.clinica.paciente.exception.DuplicateResourceException;
import com.clinica.paciente.exception.ResourceNotFoundException;
import com.clinica.paciente.model.Paciente;
import com.clinica.paciente.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    private static final Logger log = LoggerFactory.getLogger(PacienteService.class);

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    private PacienteDTO toDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNombres(paciente.getNombres());
        dto.setApellidos(paciente.getApellidos());
        dto.setRut(paciente.getRut());
        dto.setEmail(paciente.getEmail());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        return dto;
    }

    private Paciente toEntity(PacienteCreateDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNombres(dto.getNombres());
        paciente.setApellidos(dto.getApellidos());
        paciente.setRut(dto.getRut());
        paciente.setEmail(dto.getEmail());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getUserId() != null) {
            paciente.setUserId(dto.getUserId());
        }
        return paciente;
    }

    public PacienteDTO crear(PacienteCreateDTO request) {
        log.info("Creando paciente: {}, {}", request.getNombres(), request.getApellidos());

        // Validar que no exista por RUT o email
        if (repository.findByRut(request.getRut()).isPresent()) {
            throw new RuntimeException("Ya existe un paciente con el RUT: " + request.getRut());
        }
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un paciente con el email: " + request.getEmail());
        }

        Paciente paciente = toEntity(request);
        Paciente guardado = repository.save(paciente);
        log.info("Paciente creado con id={}", guardado.getId());

        return toDTO(guardado);
    }

    public List<PacienteDTO> listarTodos() {
        log.info("Listando todos los pacientes");
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PacienteDTO obtenerPorId(Long id) {
        log.info("Buscando paciente con id={}", id);
        Paciente paciente = repository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Paciente no encontrado con id={}", id);
                        return new ResourceNotFoundException("Paciente no encontrado con id: " + id);
                    });
        return toDTO(paciente);
    }

    public PacienteDTO obtenerPorUserId(Long userId) {
        log.info("Buscando paciente por userId={}", userId);
        Paciente paciente = repository.findByUserId(userId)
                    .orElseThrow(() -> {
                        log.error("Paciente no encontrado para userId={}", userId);
                        return new ResourceNotFoundException("Paciente no encontrado para el userId: " + userId);
                    });
        return toDTO(paciente);
    }

    public PacienteDTO actualizar(Long id, PacienteCreateDTO request) {
        log.info("Actualizando paciente id={}", id);

            Paciente existente = repository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Paciente no encontrado con id={}", id);
                        return new ResourceNotFoundException("Paciente no encontrado con id: " + id);
                    });

        if (!existente.getEmail().equals(request.getEmail()) &&
                repository.findByEmail(request.getEmail()).isPresent()) {
                log.warn("Intento de actualizar paciente con email duplicado: {}", request.getEmail());
                throw new DuplicateResourceException("El email ya está registrado por otro paciente");
        }

        existente.setNombres(request.getNombres());
        existente.setApellidos(request.getApellidos());
        existente.setEmail(request.getEmail());
        existente.setFechaNacimiento(request.getFechaNacimiento());
        if (request.getUserId() != null) {
            existente.setUserId(request.getUserId());
        }

        Paciente actualizado = repository.save(existente);
        log.info("Paciente id={} actualizado", id);

        return toDTO(actualizado);
    }

    public void eliminar(Long id) {
        log.info("Eliminando paciente id={}", id);
        if (!repository.existsById(id)) {
              log.error("Paciente no encontrado para eliminar, id={}", id);
              throw new ResourceNotFoundException("Paciente no encontrado con id: " + id);
        }
        repository.deleteById(id);
           log.info("Paciente id={} eliminado exitosamente", id);
    }
}