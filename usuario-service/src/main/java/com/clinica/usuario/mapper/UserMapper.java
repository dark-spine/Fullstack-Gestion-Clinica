package com.clinica.usuario.mapper;

import com.clinica.usuario.dto.UserRequestDTO;
import com.clinica.usuario.dto.UserResponseDTO;
import com.clinica.usuario.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO dto);
    UserResponseDTO toResponse(User entity);
}
