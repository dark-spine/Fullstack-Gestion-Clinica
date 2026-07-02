package com.clinica.usuario.service;

import com.clinica.usuario.dto.UsuarioCreateDTO;
import com.clinica.usuario.dto.UsuarioDTO;
import com.clinica.usuario.model.Usuario;
import com.clinica.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.clinica.usuario.exception.DuplicateResourceException;
import com.clinica.usuario.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository repository) { this.repository = repository; }

    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setRol(u.getRol());
        return dto;
    }

    public UsuarioDTO crear(UsuarioCreateDTO request) {
        log.info("Creando usuario con username: {}", request.getUsername());

        // Validar que no exista un usuario con el mismo username o email
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Intento de crear usuario con username duplicado: {}", request.getUsername());
            throw new DuplicateResourceException("Ya existe un usuario con el username: " + request.getUsername());
        }
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Intento de crear usuario con email duplicado: {}", request.getEmail());
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + request.getEmail());
        }

        Usuario u = new Usuario();
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setPassword(encoder.encode(request.getPassword()));
        u.setRol(request.getRol() != null ? request.getRol() : "PACIENTE");
        u.setCreatedAt(LocalDateTime.now());

        Usuario guardado = repository.save(u);
        log.info("Usuario creado exitosamente con id={}", guardado.getId());
        return toDTO(guardado);
    }

    public List<UsuarioDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public UsuarioDTO obtener(Long id) {
        log.info("Buscando usuario con id={}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con id={}", id);
                    return new ResourceNotFoundException("Usuario no encontrado con id: " + id);
                });
        return toDTO(usuario);
    }
}