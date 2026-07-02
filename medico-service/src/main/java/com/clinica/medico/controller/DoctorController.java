package com.clinica.medico.controller;

import com.clinica.medico.dto.DoctorRequestDTO;
import com.clinica.medico.dto.DoctorResponseDTO;
import com.clinica.medico.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Medicos",
    description = "Operaciones de gestión de médicos, especialidades y datos profesionales"
)
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService service;

    @Operation(
        summary = "Registrar un nuevo médico",
        description = "Crea el registro de un nuevo médico con sus datos personales, licencia y especialidad."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Médico registrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o licencia duplicada"
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDTO createDoctor(@Valid @RequestBody DoctorRequestDTO dto) {
        return service.createDoctor(dto);
    }

    @Operation(
        summary = "Listar todos los médicos",
        description = "Retorna la lista completa de médicos registrados en el sistema con sus especialidades."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de médicos obtenida exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorResponseDTO.class))
    )
    @GetMapping
    public List<DoctorResponseDTO> getDoctors() {
        return service.listDoctors();
    }

    @Operation(
        summary = "Contar total de médicos",
        description = "Retorna el número total de médicos registrados en el sistema."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Total de médicos",
        content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", example = "15"))
    )
    @GetMapping("/count")
    public Long countDoctors() {
        return service.countDoctors();
    }
}
