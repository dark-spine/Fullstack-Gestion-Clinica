package com.clinica.pagos.controller;

import com.clinica.pagos.dto.PaymentRequestDTO;
import com.clinica.pagos.dto.PaymentResponseDTO;
import com.clinica.pagos.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Pagos",
    description = "Operaciones de gestión de pagos y procesamiento de transacciones financieras"
)
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @Operation(
        summary = "Procesar un nuevo pago",
        description = "Crea y procesa un nuevo pago para una cita. El pago se registra en el sistema y se retorna con un ID único."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Pago procesado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos (monto negativo, cita no válida, etc.)"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor al procesar el pago"
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDTO process(@RequestBody PaymentRequestDTO dto) {
        return service.processPayment(dto);
    }

    @Operation(
        summary = "Reembolsar un pago",
        description = "Procesa un reembolso para un pago existente. El pago debe estar en estado PROCESSED para poder ser reembolsado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Reembolso procesado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pago no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "No se puede reembolsar un pago en ese estado"
        )
    })
    @PostMapping("/{id}/refund")
    public PaymentResponseDTO refund(
            @Parameter(
                description = "ID del pago a reembolsar",
                required = true
            )
            @PathVariable("id") Long id
    ) {
        return service.refundPayment(id);
    }

    @Operation(
        summary = "Listar todos los pagos",
        description = "Retorna la lista completa de pagos registrados en el sistema, incluyendo tanto pagos procesados como reembolsados."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de pagos obtenida exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))
    )
    @GetMapping
    public List<PaymentResponseDTO> list() {
        return service.listPayments();
    }

    @Operation(
        summary = "Contar total de pagos",
        description = "Retorna el número total de pagos procesados en el sistema."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Total de pagos",
        content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", example = "42"))
    )
    @GetMapping("/count")
    public Long count() {
        return service.countPayments();
    }

    @Operation(
        summary = "Calcular ingresos totales",
        description = "Calcula el monto total de ingresos por pagos procesados. No incluye pagos reembolsados."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Total de ingresos",
        content = @Content(mediaType = "application/json", schema = @Schema(type = "number", example = "15750.50"))
    )
    @GetMapping("/revenue")
    public Double revenue() {
        return service.totalRevenue();
    }

    @Operation(
        summary = "Obtener resumen de pago por cita",
        description = "Retorna el resumen de pagos asociado a una cita específica, incluyendo monto, estado y fecha de procesamiento."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Resumen obtenido exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cita no encontrada o sin pagos asociados"
        )
    })
    @GetMapping("/summary")
    public Object summary(
            @Parameter(
                description = "ID de la cita para obtener su resumen de pagos",
                required = true,
                example = "1"
            )
            @RequestParam("appointmentId") Long appointmentId
    ) {
        return service.paymentSummary(appointmentId);
    }
}
