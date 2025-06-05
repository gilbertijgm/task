package com.proyecto.task.persistence;

import com.proyecto.task.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITaskDAO {

    //Metodo para guardar
    Task saveTask(Task task);

    //Metodo para actualizar
    Task updateTask(Task task);

    //Metodo para guardar una lista de tareas
    List<Task> saveListTask(List<Task> listaTask);

    //Metodo para eliminar
    void deleteTaskById(Long id);

    //Metodo para actulizar un campo parcial (PATCH)
    Task statusUpdate(Task task);

    //Metodo para listar todos los recursos con paginacion
    Page<Task> tasks(Pageable pageable);

    //Metodo para listar todos los recursos
    List<Task> findAllTask();

    //Metodo para buescar recurso por id
    Optional<Task> findTaskById(Long id);

    //Metodo para filtrar tareas por estado
    Page<Task> findByStatus(Pageable pageable,Task.Status status);

    //Metdodo para filtrar tareas vencidas
    Page<Task> findByExpirationDateBefore(Pageable pageable, LocalDate fecha);

    //Metodo para listar tareas que vencen hoy
    Page<Task> findByExpirationDate(Pageable pageable,LocalDate fecha);

    //Metodo para filtrar tareas dentro de una rango de fecha
    Page<Task> findByExpirationDateBetween(Pageable pageable, LocalDate desde, LocalDate hasta);

    //Metodo para filtrar tareas por palabras clave
    Page<Task> findByTitleContaining(Pageable pageable, String texto);

}
