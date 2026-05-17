package com.clinica.usuario.service;

import com.clinica.usuario.dto.UserRequestDTO;
import com.clinica.usuario.dto.UserResponseDTO;
import com.clinica.usuario.mapper.UserMapper;
import com.clinica.usuario.model.User;
import com.clinica.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = mapper.toEntity(dto);
        User saved = repository.save(user);
        return mapper.toResponse(saved);
    }

    public List<UserResponseDTO> listUsers() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}
