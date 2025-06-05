package com.proyecto.task.persistence.implementacion;

import com.proyecto.task.entities.Task;
import com.proyecto.task.persistence.ITaskDAO;
import com.proyecto.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskDAOImpl implements ITaskDAO {

//    @Autowired
//    private TaskRepository taskRepository;

    private final TaskRepository taskRepository;

//    public TaskDAOImpl(TaskRepository taskRepository) {
//        this.taskRepository = taskRepository;
//    }

    //TODOS ESTOS IMPLEMENTACIONES LLAMAN LOS METODOS DEL REPOSITORIO

    //Metodo para guardar
    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);

    }

    //Metodo para actualizar
    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    //Metodo para guardar una lista de tareas
    @Override
    public List<Task> saveListTask(List<Task> listaTask) {

        List<Task> lista = taskRepository.saveAll(listaTask);

        return lista;
    }

    //Metodo para eliminar
    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    //Metodo para actulizar un campo parcial (PATCH)
    @Override
    public Task statusUpdate(Task task) {
        return taskRepository.save(task);
    }

    //Metodo para listar todos los recursos con paginacion
    @Override
    public Page<Task> tasks(Pageable pageable) {
        Page<Task> pageTask = taskRepository.findAll(pageable);

        return pageTask;
    }

    //Metodo para listar todos los recursos
    @Override
    public List<Task> findAllTask() {
        List<Task> list_task = taskRepository.findAll();

        return list_task;
    }

    //Metodo para buescar recurso por id
    @Override
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    //Metodo para filtrar tareas por estado
    @Override
    public Page<Task> findByStatus(Pageable pageable,Task.Status status) {
        return taskRepository.findByStatus(pageable,status);
    }

    //Metdodo para filtrar tareas vencidas
    @Override
    public Page<Task> findByExpirationDateBefore(Pageable pageable, LocalDate fecha) {
        return taskRepository.findByExpirationDateBefore(pageable, fecha);
    }

    //Metodo para listar tareas que vencen hoy
    @Override
    public Page<Task> findByExpirationDate(Pageable pageable,LocalDate fecha) {
        return taskRepository.findByExpirationDate(pageable,fecha);
    }

    //Metodo para filtrar tareas dentro de una rango de fecha
    @Override
    public Page<Task> findByExpirationDateBetween(Pageable pageable, LocalDate desde, LocalDate hasta) {
        return taskRepository.findByExpirationDateBetweenOrderByExpirationDateAsc(pageable, desde, hasta);
    }

    //Metodo para filtrar tareas por palabras clave
    @Override
    public Page<Task> findByTitleContaining(Pageable pageable,String texto) {
        return taskRepository.findByTitleContaining(pageable, texto);
    }

}
