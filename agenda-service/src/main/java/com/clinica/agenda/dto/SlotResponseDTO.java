package com.clinica.agenda.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotResponseDTO {
    private Long id;
    private Long doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer durationMinutes;
}
