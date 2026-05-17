package com.clinica.agenda.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotRequestDTO {
    private Long doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
}
