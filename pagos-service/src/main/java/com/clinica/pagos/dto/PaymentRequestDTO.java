package com.clinica.pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para procesar un pago")
public class PaymentRequestDTO {
    @Schema(
        description = "ID de la cita asociada al pago",
        example = "1",
        required = true
    )
    private Long appointmentId;

    @Schema(
        description = "Monto a pagar",
        example = "150.50",
        required = true
    )
    private BigDecimal amount;

    @Schema(
        description = "Tipo de pago (CREDIT_CARD, CASH, BANK_TRANSFER)",
        example = "CREDIT_CARD",
        required = true
    )
    private String type;
}
