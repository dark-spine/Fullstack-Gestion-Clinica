package com.clinica.pagos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoCreateDTO {
    @NotNull(message = "La cita es obligatoria")
    private Long citaId;

    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El monto es obligatorio")
    private Double monto;

    private String concepto;
}