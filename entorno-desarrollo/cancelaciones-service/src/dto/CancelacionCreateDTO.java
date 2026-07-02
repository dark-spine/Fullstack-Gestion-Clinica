package com.clinica.cancelaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelacionCreateDTO {
    @NotNull(message = "La cita es obligatoria")
    private Long citaId;

    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
}