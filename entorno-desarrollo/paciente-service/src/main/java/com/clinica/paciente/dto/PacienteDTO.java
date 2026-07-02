package com.clinica.paciente.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String rut;
    private String email;
    private LocalDate fechaNacimiento;
}