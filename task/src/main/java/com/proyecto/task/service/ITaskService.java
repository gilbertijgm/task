package com.proyecto.task.service;

import com.proyecto.task.controller.dtos.TaskDTO;
import com.proyecto.task.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITaskService {

    //Metodo para guardar
    TaskDTO saveTask(TaskDTO task);

    //Metodo para actualizar
    TaskDTO updateTask(TaskDTO task, Long id);

    //Metodo para guardar una lista de tareas
    void saveListTask(List<TaskDTO> listaTask);

    //Metodo para eliminar
    void deleteTaskById(Long id);

    //Metodo para actulizar un campo parcial (PATCH)
    TaskDTO updateStatus(String newStatus, Long id);

    //Metodo para listar todos los recursos con paginacion
    Page<TaskDTO> tasks(Pageable pageable);

    //Metodo para listar todos los recursos
    List<TaskDTO> findAllTask();

    //Metodo para buescar recurso por id
    TaskDTO findTaskById(Long id);

    //Metodo para filtrar tareas por estado
    Page<TaskDTO> findByStatus(Pageable pageable,Task.Status status);

    //Metdodo para filtrar tareas vencidas
    Page<TaskDTO> findByExpirationDateBefore(Pageable pageable);

    //Metodo para listar tareas que vencen hoy
    Page<TaskDTO> findByExpirationDate(Pageable pageable);

    //Metodo para filtrar tareas dentro de una rango de fecha
    Page<TaskDTO> findByExpirationDateBetween(Pageable pageable, LocalDate desde, LocalDate hasta);

    //Metodo para filtrar tareas por palabras clave
    Page<TaskDTO> findByTitleContaining(Pageable pageable, String texto);

}
