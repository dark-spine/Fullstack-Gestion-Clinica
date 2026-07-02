package com.clinica.agenda.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wait_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitListEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private Long doctorId;
    private String reason;
    private LocalDateTime requestedAt;
}
