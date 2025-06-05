package com.proyecto.task.service.mappers;


import com.proyecto.task.controller.dtos.TaskDTO;
import com.proyecto.task.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring") // Esto le dice a Spring que lo gestione como un bean
public interface TaskMapper {

    // Método para convertir entidad a DTO
    TaskDTO toDto(Task task);

    // Método para convertir DTO a entidad
    Task toEntity(TaskDTO dto);

    // También podés mapear listas automáticamente
    List<TaskDTO> toDtoList(List<Task> tasks);
    List<Task> toEntityList(List<TaskDTO> dtos);

    @Mapping(target = "idTask", ignore = true) //evitar sobrescritura del ID u otros campos sensibles).
    void updateEntityFromDto(TaskDTO dto, @MappingTarget Task entity);
}
