package com.clinica.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateDTO {
    @NotBlank(message = "El username es obligatorio")
    private String username;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    private String rol;
}