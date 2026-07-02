package com.clinica.cancelaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private String estado;
}