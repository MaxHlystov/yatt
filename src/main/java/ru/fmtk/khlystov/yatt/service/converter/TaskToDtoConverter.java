package ru.fmtk.khlystov.yatt.service.converter;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.Task;
import ru.fmtk.khlystov.yatt.dto.TaskDto;

import static ru.fmtk.khlystov.yatt.dto.UserDto.NULL_USER;

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

    public Mono<TaskDto> toDto(Task task) {
        final var builder = TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .modifiedAt(task.getModifiedAt());
        return statusToDtoConverter.toDto(task.getStatusId())
                .map(builder::status)
                .then(userToDtoConverter.toDto(task.getAssigneeId()))
                .map(assignee -> builder.assignee(assignee == NULL_USER ? null : assignee))
                .then(userToDtoConverter.toDto(task.getCreatedBy()))
                .map(createdBy -> builder.createdBy(createdBy == NULL_USER ? null : createdBy))
                .then(userToDtoConverter.toDto(task.getModifiedBy()))
                .map(modifiedBy -> builder.modifiedBy(modifiedBy == NULL_USER ? null : modifiedBy))
                .map(TaskDto.TaskDtoBuilder::build);
    }

    public Task updateEntity(Task taskToUpdate, TaskDto taskDto) {
        return Task.builder()
                .id(taskToUpdate.getId())
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .statusId(taskDto.getStatus().getId())
                .assigneeId(taskDto.getAssignee().getId())
                .createdAt(taskToUpdate.getCreatedAt())
                .createdBy(taskToUpdate.getCreatedBy())
                .modifiedAt(null)
                .modifiedBy(taskDto.getModifiedBy().getId())
                .build();
    }
}
