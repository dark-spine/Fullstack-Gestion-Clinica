package com.clinica.facturacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDTO {
    private Long id;
    private Long citaId;
    private Long pacienteId;
    private Long medicoId;
    private Double montoTotal;
    private String metodoPago;
    private Boolean pagada;
    private LocalDateTime fechaPago;
    private LocalDateTime createdAt;
}