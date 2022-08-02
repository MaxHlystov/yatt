package ru.fmtk.khlystov.yatt.service.converter;

import org.springframework.stereotype.Service;
import ru.fmtk.khlystov.yatt.domain.Task;
import ru.fmtk.khlystov.yatt.dto.TaskDto;

@Service
public class TaskToDtoConverter {

    private final UserToDtoConverter userToDtoConverter;
    private final StatusToDtoConverter statusToDtoConverter;

    public TaskToDtoConverter(UserToDtoConverter userToDtoConverter,
                              StatusToDtoConverter statusToDtoConverter
    ) {
        this.userToDtoConverter = userToDtoConverter;
        this.statusToDtoConverter = statusToDtoConverter;
    }

    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .status(statusToDtoConverter.toDto(task.getStatus()))
                .assignee(userToDtoConverter.toDto(task.getAssignee()))
                .createdAt(task.getCreatedAt())
                .createdBy(userToDtoConverter.toDto(task.getCreatedBy()))
                .modifiedAt(task.getModifiedAt())
                .modifiedBy(userToDtoConverter.toDto(task.getModifiedBy()))
                .build();
    }

    public Task updateEntity(Task taskToUpdate, TaskDto taskDto) {
        return Task.builder()
                .id(taskToUpdate.getId())
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .status(statusToDtoConverter.toEntity(taskDto.getStatus()))
                .assignee(userToDtoConverter.toEntity(taskDto.getAssignee()))
                .createdAt(taskToUpdate.getCreatedAt())
                .createdBy(taskToUpdate.getCreatedBy())
                .modifiedAt(null)
                .modifiedBy(userToDtoConverter.toEntity(taskDto.getModifiedBy()))
                .build();
    }
}
