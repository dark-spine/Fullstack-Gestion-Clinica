package com.clinica.facturacion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaCreateDTO {
    @NotNull(message = "La cita es obligatoria")
    private Long citaId;

    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El médico es obligatorio")
    private Long medicoId;

    @NotNull(message = "El monto total es obligatorio")
    private Double montoTotal;

    private String metodoPago;
}