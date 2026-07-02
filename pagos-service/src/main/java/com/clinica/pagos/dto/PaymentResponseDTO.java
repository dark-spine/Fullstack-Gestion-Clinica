package com.clinica.pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta de un pago procesado")
public class PaymentResponseDTO {
    @Schema(
        description = "ID único del pago",
        example = "42"
    )
    private Long id;

    @Schema(
        description = "ID de la cita asociada",
        example = "1"
    )
    private Long appointmentId;

    @Schema(
        description = "Monto del pago",
        example = "150.50"
    )
    private BigDecimal amount;

    @Schema(
        description = "Estado del pago (PROCESSED, REFUNDED, PENDING)",
        example = "PROCESSED"
    )
    private String status;

    @Schema(
        description = "Tipo de pago utilizado",
        example = "CREDIT_CARD"
    )
    private String type;

    @Schema(
        description = "Fecha y hora de procesamiento",
        example = "2026-06-17T10:30:00"
    )
    private LocalDateTime processedAt;
}
