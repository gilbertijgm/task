package com.proyecto.task.controller.dtos;

import com.proyecto.task.advice.GlobalExceptionHandler;
import com.proyecto.task.entities.Task;
import com.proyecto.task.validation.NoWeekend;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long idTask;

    @NotBlank(message = "Campo requerido, Titulo no puede estar vacio")
    @Size(min = 2, max = 50)
    private String title;

    @NotBlank(message = "Campo requerido, Descripcion no puede estar vacio")
    @Size(min = 2, max = 250)
    private String description;

    @NotNull(message = "Campo requerido, Descripcion no puede estar vacio")
    @FutureOrPresent(message = "La fecha de exipiracion no puede ser anterior a hoy")
    @NoWeekend
    private LocalDate expirationDate;

    @NotNull(message = "Campo requerido, Estado no puede estar vacio")
    @Enumerated(EnumType.STRING)
    private Task.Status status;

    public enum Status{
        Pendiente, Desarrollo, Completada, Cancelada
    }
}
