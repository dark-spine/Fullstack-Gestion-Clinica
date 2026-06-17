package com.clinica.citas.controller;

import com.clinica.citas.dto.AppointmentRequestDTO;
import com.clinica.citas.dto.AppointmentResponseDTO;
import com.clinica.citas.service.AppointmentService;
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
    name = "Citas",
    description = "Operaciones de gestión de citas médicas, confirmación y cancelación"
)
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService service;

    @Operation(
        summary = "Crear una nueva cita",
        description = "Crea una nueva cita médica. La cita se asigna a un médico disponible en la fecha y hora solicitada."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Cita creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o médico no disponible"
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponseDTO create(@RequestBody AppointmentRequestDTO dto) {
        return service.createAppointment(dto);
    }

    @Operation(
        summary = "Cancelar una cita",
        description = "Cancela una cita existente. La cita debe estar en estado CONFIRMED o PENDING."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cita cancelada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cita no encontrada"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "No se puede cancelar una cita en ese estado"
        )
    })
    @PostMapping("/{id}/cancel")
    public AppointmentResponseDTO cancel(
        @Parameter(
            description = "ID de la cita a cancelar",
            required = true,
            example = "1"
        )
        @PathVariable("id") Long id
    ) {
        return service.cancelAppointment(id);
    }

    @Operation(
        summary = "Listar todas las citas",
        description = "Retorna la lista completa de citas registradas en el sistema."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de citas obtenida exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentResponseDTO.class))
    )
    @GetMapping
    public List<AppointmentResponseDTO> getAll() {
        return service.listAppointments();
    }

    @Operation(
        summary = "Contar total de citas",
        description = "Retorna el número total de citas registradas en el sistema."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Total de citas",
        content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", example = "25"))
    )
    @GetMapping("/count")
    public Long count() {
        return service.countAppointments();
    }

    @Operation(
        summary = "Obtener tasa de inasistencia",
        description = "Calcula el porcentaje de citas que no tuvieron presentación del paciente (no-show)."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Tasa de inasistencia",
        content = @Content(mediaType = "application/json", schema = @Schema(type = "number", example = "0.15"))
    )
    @GetMapping("/no-show-rate")
    public Double noShowRate() {
        return service.noShowRate();
    }
}
