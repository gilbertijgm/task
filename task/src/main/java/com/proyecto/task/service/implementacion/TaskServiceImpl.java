package com.proyecto.task.service.implementacion;

import com.proyecto.task.advice.NoContentException;
import com.proyecto.task.advice.ResourceNotFoundException;
import com.proyecto.task.advice.ServiceUtils;
import com.proyecto.task.controller.dtos.TaskDTO;
import com.proyecto.task.entities.Task;
import com.proyecto.task.persistence.ITaskDAO;
import com.proyecto.task.service.ITaskService;
import com.proyecto.task.service.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

//    @Autowired
//    private ITaskDAO taskDAO;

    private final ITaskDAO taskDAO;
    private final TaskMapper taskMapper;

//    public TaskServiceImpl(TaskMapper taskMapper, ITaskDAO taskDAO) {
//        this.taskMapper = taskMapper;
//        this.taskDAO = taskDAO;
//    }

    //    @Override
//    public TaskDTO saveTask(TaskDTO taskDTO) {
//
//        Task task = Task.builder()
//                .title(taskDTO.getTitle())
//                .description(taskDTO.getDescription())
//                .expirationDate(taskDTO.getExpirationDate())
//                .status(taskDTO.getStatus())
//                .build();
//
//        task = taskDAO.saveTask(task);
//
//
//        return TaskDTO.builder()
//                .idTask(task.getIdTask())
//                .title(task.getTitle())
//                .description(task.getDescription())
//                .expirationDate(task.getExpirationDate())
//                .status(task.getStatus())
//                .build();
//    }
    @Override
    public TaskDTO saveTask(TaskDTO taskDTO) {
        //Convertimos el dto recibido a una entidad
        Task task = taskMapper.toEntity(taskDTO);

        //guardamos en la bd
        task = taskDAO.saveTask(task);

        //convertimos devuelta la entidad a DTO para gestionar la respuesta
        return taskMapper.toDto(task);
    }

    //    @Override
//    public TaskDTO updateTask(TaskDTO taskDTO, Long id) {
//        //buscamos la tarea a modificar por el id recibido
//        Task task = taskDAO.findTaskById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));
//
//        //Actulizamos los campos de la entidad con los atributos del DTO que recibimos
//        task.setTitle(taskDTO.getTitle());
//        task.setDescription(taskDTO.getDescription());
//        task.setExpirationDate(taskDTO.getExpirationDate());
//        task.setStatus(taskDTO.getStatus());
//
//        //guardamos la entidad actualizada
//        Task updateTask = taskDAO.updateTask(task);
//
//        //convertimos la entidad actualizada a DTO para la respuesta
//
//        return TaskDTO.builder()
//                .idTask(updateTask.getIdTask())
//                .title(updateTask.getTitle())
//                .description(updateTask.getDescription())
//                .expirationDate(updateTask.getExpirationDate())
//                .status(updateTask.getStatus())
//                .build();
//
//    }
    @Override
    public TaskDTO updateTask(TaskDTO taskDTO, Long id) {
        //buscamos la tarea a modificar por el id recibido
        Task task = taskDAO.findTaskById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));

        //Mapeamos el dto recibido a la entidad, usando el metodo creado en el mapper
        //Esta línea actualiza el objeto original sin crear uno nuevo.
        // Es el enfoque más limpio, eficiente y seguro cuando querés actualizar parcialmente una entidad sin pisar todo.
        taskMapper.updateEntityFromDto(taskDTO, task);

        //guardamos la entidad actualizada
        Task updateTask = taskDAO.updateTask(task);

        //convertimos la entidad actualizada a DTO para la respuesta
        return taskMapper.toDto(updateTask);

    }

    @Override
    public void saveListTask(List<TaskDTO> listaTask) {
        List<Task> listTask = listaTask.stream()
                .map(dto -> new Task(
                        dto.getIdTask(),
                        dto.getTitle(),
                        dto.getDescription(),
                        dto.getExpirationDate(),
                        dto.getStatus()
                ))
                .collect(Collectors.toList());

        taskDAO.saveListTask(listTask);
    }

    @Override
    public TaskDTO updateStatus(String newStatus, Long id) {
        //buscamos la tarea a modificar por el id recibido
        Task task = taskDAO.findTaskById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));


//        Validación:
//        valueOf() lanza una excepción si el valor no coincide con ningún enum.
        Task.Status status = Task.Status.valueOf(newStatus);
        //seteamos el valor a la tarea
        task.setStatus(status);

        //guardamos la entidad actualizada
        Task updateTask = taskDAO.updateTask(task);

        //convertimos la entidad actualizada a DTO para la respuesta
        return taskMapper.toDto(updateTask);
    }

    @Override
    public void deleteTaskById(Long id) {
        //buscamos la tarea a eliminar por el id recibido
        Task task = taskDAO.findTaskById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));

        taskDAO.deleteTaskById(task.getIdTask());
    }

    @Override
    public Page<TaskDTO> tasks(String estado, String palabraClave, LocalDate fechaInicio, LocalDate fechaFin, boolean vencidas, boolean vencenHoy, Pageable pageable) {
        //Obetenemos el listado de tareas paginadas
        Page<Task> taskPage = taskDAO.tasks(estado, palabraClave, fechaInicio, fechaFin, vencidas, vencenHoy, pageable);

        //Convertimos las entidades en DTO usando el mapper creado con .getContent() que trae solo las tareas y no la informacion de paginacion
        List<TaskDTO> dtoList = taskMapper.toDtoList(taskPage.getContent());


        //validamos cuando la lista esté vacía
        ServiceUtils.validateNotEmpty(dtoList, "No hay tareas registradas.");

        // Retornamos un nuevo Page con los DTOs y la info de paginación original
        return new PageImpl<>(dtoList, pageable, taskPage.getTotalElements());
    }

    @Override
    public List<TaskDTO> findAllTask() {
        //obtenemos las tareas
        List<Task> listTask = taskDAO.findAllTask();

        //validamos cuando la lista este vacia
        ServiceUtils.validateNotEmpty(listTask, "No hay tareas registradas.");

        //las retornamos mappeadas a DTO
        return taskMapper.toDtoList(listTask);
    }

    @Override
    public TaskDTO findTaskById(Long id) {
        return taskDAO.findTaskById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));
    }


    @Override
    public Page<TaskDTO> findByStatus(Pageable pageable,Task.Status status) {
        Page<Task> listTask = taskDAO.findByStatus(pageable,status);

        List<TaskDTO> dtoList = taskMapper.toDtoList(listTask.getContent());

        ServiceUtils.validateNotEmpty(dtoList, "No hay tareas registradas con ese estado.");

        return new PageImpl<>(dtoList, pageable, listTask.getTotalElements());
    }

    @Override
    public Page<TaskDTO> findByExpirationDateBefore(Pageable pageable) {

        //obtenemos la fecha de hoy
        LocalDate hoy = LocalDate.now();

        Page<Task> listTaskExpired = taskDAO.findByExpirationDateBefore(pageable, hoy);

        List<TaskDTO> dtoList = taskMapper.toDtoList(listTaskExpired.getContent());

        //validamos cuando la lista este vacia
        ServiceUtils.validateNotEmpty(dtoList, "No hay tareas vencidas.");

        return new PageImpl<>(dtoList, pageable, listTaskExpired.getTotalElements());
    }

    @Override
    public Page<TaskDTO> findByExpirationDate(Pageable pageable) {

        //obtenemos la fecha de hoy
        LocalDate hoy = LocalDate.now();

        Page<Task> listTaskExpired = taskDAO.findByExpirationDate(pageable, hoy);

        List<TaskDTO> dtoList = taskMapper.toDtoList(listTaskExpired.getContent());

        //validamos cuando la lista este vacia
        ServiceUtils.validateNotEmpty(dtoList, "No hay tareas.");

        return new PageImpl<>(dtoList, pageable, listTaskExpired.getTotalElements());
    }

    @Override
    public Page<TaskDTO> findByExpirationDateBetween(Pageable pageable, LocalDate desde, LocalDate hasta) {
        Page<Task> listTaskExpiredBetween = taskDAO.findByExpirationDateBetween(pageable, desde, hasta);

        List<TaskDTO> dtoList = taskMapper.toDtoList(listTaskExpiredBetween.getContent());

        //validamos cuando la lista este vacia
        ServiceUtils.validateNotEmpty(dtoList, "No hay tareas en ese rango de fechas");

        return new PageImpl<>(dtoList, pageable, listTaskExpiredBetween.getTotalElements());
    }

    @Override
    public Page<TaskDTO> findByTitleContaining(Pageable pageable,String texto) {
        Page<Task> listTaskContaining = taskDAO.findByTitleContaining(pageable, texto);

        List<TaskDTO> dtoList = taskMapper.toDtoList(listTaskContaining.getContent());

        //validamos cuando la lista este vacia
        ServiceUtils.validateNotEmpty(dtoList, "No hay tareas encontradas.");

        return new PageImpl<>(dtoList, pageable, listTaskContaining.getTotalElements());
    }

}