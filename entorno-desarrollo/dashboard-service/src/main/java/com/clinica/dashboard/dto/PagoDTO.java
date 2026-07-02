package com.clinica.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
    private Long id;
    private Double monto;
    private String estado;
    private LocalDateTime fechaPago;
}