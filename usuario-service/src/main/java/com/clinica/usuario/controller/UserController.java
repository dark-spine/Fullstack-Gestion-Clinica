package com.clinica.usuario.controller;

import com.clinica.usuario.dto.UserRequestDTO;
import com.clinica.usuario.dto.UserResponseDTO;
import com.clinica.usuario.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody UserRequestDTO dto) {
        return service.createUser(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getUsers() {
        return service.listUsers();
    }
}
