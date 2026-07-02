package com.clinica.facturacion.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequestDTO {
    private Long appointmentId;
    private BigDecimal amount;
    private Boolean retained;
}
