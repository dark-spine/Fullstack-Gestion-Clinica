package com.clinica.agenda.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_slots", uniqueConstraints = {@UniqueConstraint(columnNames = {"doctor_id", "start_time"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer durationMinutes;
}
