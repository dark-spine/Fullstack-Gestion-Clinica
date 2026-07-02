package com.clinica.facturacion.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponseDTO {
    private Long id;
    private Long appointmentId;
    private String invoiceNumber;
    private BigDecimal amount;
    private LocalDate issueDate;
    private Boolean retained;
}
