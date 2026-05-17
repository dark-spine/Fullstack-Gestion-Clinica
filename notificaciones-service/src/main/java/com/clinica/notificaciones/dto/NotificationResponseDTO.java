package com.clinica.notificaciones.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {
    private Long id;
    private String target;
    private String channel;
    private String message;
    private LocalDateTime createdAt;
}
