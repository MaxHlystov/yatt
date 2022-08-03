package ru.fmtk.khlystov.yatt.service.converter;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.Task;
import ru.fmtk.khlystov.yatt.dto.TaskDto;

@Service
public class TaskToStringConverter {

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TaskToDtoConverter taskToDtoConverter;

    public TaskToStringConverter(TaskToDtoConverter taskToDtoConverter) {
        this.taskToDtoConverter = taskToDtoConverter;
    }

    public String headerString(TaskDto taskDto) {
        return "Задача №" + taskDto.getId() + "'" + taskDto.getName() + "'";
    }

    public String headerString(Task task) {
        return "Задача №" + task.getId() + "'" + task.getName() + "'";
    }

    public String getFullDescription(TaskDto taskDto) {
        return "Задача №" + taskDto.getId() + " '" + taskDto.getName() + "'\n" +
                taskDto.getDescription() + "\n" +
                "Создана " + taskDto.getCreatedAt().format(DATE_FORMATTER) +
                " " + taskDto.getCreatedBy().getName() +
                (taskDto.getAssignee() == null ? "" : "\nОтветственный: " + taskDto.getAssignee().getName());
    }

    public Mono<String> getFullDescription(Task task) {
        return taskToDtoConverter.toDto(task)
                .map(this::getFullDescription);
    }
}
