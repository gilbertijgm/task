package com.proyecto.task.repository;

import com.proyecto.task.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //Metodo para obtener todas las tareas aplicando paginacion
    Page<Task> findAll(Pageable pageable);

    //metodo para obtener las tareas por su estado
    Page<Task> findByStatus(Pageable pageable,Task.Status status);

    //meteodo para obtener las tareas que estan vencidas
    Page<Task> findByExpirationDateBefore(Pageable pageable, LocalDate fecha);

    //metodo para obtener las tareas que vencen hoy
    Page<Task> findByExpirationDate(Pageable pageable, LocalDate fecha);

    //metodo para obtener las tareas dentro de un rango de fechas indicado
    Page<Task> findByExpirationDateBetweenOrderByExpirationDateAsc(Pageable pageable,LocalDate desde, LocalDate hasta);

    //metodo para buscar tareas por palabra clave en titulo o descripcion (LIKE %texto%) en la consulta
    Page<Task> findByTitleContaining(Pageable pageable, String texto);

}
