package com.clinica.medico.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String specialty;

    @NotBlank
    private String office;

    @NotBlank
    private String baseSchedule;
}
