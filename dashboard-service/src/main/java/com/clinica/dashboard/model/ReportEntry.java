package com.clinica.dashboard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String value;
}
