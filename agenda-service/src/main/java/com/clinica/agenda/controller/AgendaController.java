package com.clinica.agenda.controller;

import com.clinica.agenda.dto.SlotRequestDTO;
import com.clinica.agenda.dto.SlotResponseDTO;
import com.clinica.agenda.service.AgendaService;
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

import java.time.LocalDateTime;
import java.util.List;

@Tag(
    name = "Agenda",
    description = "Operaciones de gestión de slots, disponibilidad y reservas de médicos"
)
@RestController
@RequestMapping("/api/agenda")
@RequiredArgsConstructor
public class AgendaController {
    private final AgendaService service;

    @Operation(
        summary = "Crear un nuevo slot",
        description = "Crea un nuevo slot de disponibilidad para un médico en una fecha y hora específica."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Slot creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SlotResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping("/slots")
    @ResponseStatus(HttpStatus.CREATED)
    public SlotResponseDTO createSlot(@RequestBody SlotRequestDTO dto) {
        return service.createSlot(dto);
    }

    @Operation(
        summary = "Reservar un slot disponible",
        description = "Reserva un slot disponible de un médico para una cita. El slot debe estar disponible."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Slot reservado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SlotResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Slot no disponible o médico no encontrado"
        )
    })
    @PostMapping("/slots/reserve")
    public SlotResponseDTO reserveSlot(
        @Parameter(
            description = "ID del médico cuyo slot se desea reservar",
            required = true,
            example = "1"
        )
        @RequestParam("doctorId") Long doctorId,
        @Parameter(
            description = "Hora de inicio del slot en formato ISO (ej: 2025-06-20T10:00:00)",
            required = true,
            example = "2025-06-20T10:00:00"
        )
        @RequestParam("startTime") String startTime
    ) {
        return service.reserveSlot(doctorId, LocalDateTime.parse(startTime));
    }

    @Operation(
        summary = "Obtener slots disponibles",
        description = "Retorna la lista de slots disponibles para un médico específico."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de slots disponibles obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SlotResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Médico no encontrado"
        )
    })
    @GetMapping("/slots/available")
    public List<SlotResponseDTO> getAvailableSlots(
        @Parameter(
            description = "ID del médico para obtener sus slots disponibles",
            required = true,
            example = "1"
        )
        @RequestParam("doctorId") Long doctorId
    ) {
        return service.findAvailable(doctorId);
    }
}
