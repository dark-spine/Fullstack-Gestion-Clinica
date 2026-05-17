package com.clinica.medico.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {
    private String name;
    private String specialty;
    private String office;
    private String baseSchedule;
}
