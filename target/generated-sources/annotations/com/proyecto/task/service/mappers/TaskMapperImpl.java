package com.proyecto.task.service.mappers;

import com.proyecto.task.controller.dtos.TaskDTO;
import com.proyecto.task.entities.Task;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T20:38:19-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDTO toDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDTO.TaskDTOBuilder taskDTO = TaskDTO.builder();

        taskDTO.idTask( task.getIdTask() );
        taskDTO.title( task.getTitle() );
        taskDTO.description( task.getDescription() );
        taskDTO.expirationDate( task.getExpirationDate() );
        taskDTO.status( task.getStatus() );

        return taskDTO.build();
    }

    @Override
    public Task toEntity(TaskDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Task.TaskBuilder task = Task.builder();

        task.idTask( dto.getIdTask() );
        task.title( dto.getTitle() );
        task.description( dto.getDescription() );
        task.expirationDate( dto.getExpirationDate() );
        task.status( dto.getStatus() );

        return task.build();
    }

    @Override
    public List<TaskDTO> toDtoList(List<Task> tasks) {
        if ( tasks == null ) {
            return null;
        }

        List<TaskDTO> list = new ArrayList<TaskDTO>( tasks.size() );
        for ( Task task : tasks ) {
            list.add( toDto( task ) );
        }

        return list;
    }

    @Override
    public List<Task> toEntityList(List<TaskDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Task> list = new ArrayList<Task>( dtos.size() );
        for ( TaskDTO taskDTO : dtos ) {
            list.add( toEntity( taskDTO ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(TaskDTO dto, Task entity) {
        if ( dto == null ) {
            return;
        }

        entity.setTitle( dto.getTitle() );
        entity.setDescription( dto.getDescription() );
        entity.setExpirationDate( dto.getExpirationDate() );
        entity.setStatus( dto.getStatus() );
    }
}
