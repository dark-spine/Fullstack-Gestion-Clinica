package com.clinica.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
    private Long id;
    private String numeroOrden;
    private Long citaId;
    private Long pacienteId;
    private Double monto;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaPago;
}