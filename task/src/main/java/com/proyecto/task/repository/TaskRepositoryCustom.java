package com.proyecto.task.repository;

import com.proyecto.task.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TaskRepositoryCustom {

    // Implementamos el método que permite filtrar tareas según múltiples criterios opcionales incluyendo la paginacion
    Page<Task> buscarTareasConFiltros(String estado, String palabraClave, LocalDate fechaInicio, LocalDate fechaFin, boolean vencidas, boolean vencenHoy, Pageable pageable);

}
