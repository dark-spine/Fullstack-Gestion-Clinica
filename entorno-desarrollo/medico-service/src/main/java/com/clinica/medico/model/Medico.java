package com.clinica.medico.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicos")
public class Medico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String rut;
    private String email;
    private Long especialidadId;
    private Boolean activo = true;
    private LocalDateTime createdAt;

}