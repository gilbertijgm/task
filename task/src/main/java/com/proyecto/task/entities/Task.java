package com.proyecto.task.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long idTask;

    @Column(name = "titulo", length = 60)
    private String title;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "fecha_vencimiento")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Status status;

    public enum Status{
        Pendiente, Desarrollo, Completada, Cancelada
    }
}
