package ru.fmtk.khlystov.yatt.api;

import javax.validation.Valid;

import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.dto.CreateTaskDto;
import ru.fmtk.khlystov.yatt.dto.TaskDto;
import ru.fmtk.khlystov.yatt.dto.TaskFilterDto;
import ru.fmtk.khlystov.yatt.exception.BadRequestException;
import ru.fmtk.khlystov.yatt.service.TaskService;
import ru.fmtk.khlystov.yatt.service.converter.TaskToDtoConverter;

import static ru.fmtk.khlystov.yatt.api.HeaderConstants.YATT_CREATE_TASK_UID;

@RestController
@RequestMapping("/api")
@Valid
public class TaskController {

    private final TaskService taskService;
    private final TaskToDtoConverter taskToDtoConverter;

    public TaskController(TaskService taskService, TaskToDtoConverter taskToDtoConverter) {
        this.taskService = taskService;
        this.taskToDtoConverter = taskToDtoConverter;
    }

    @PostMapping(value = "/v1/task/filter", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TaskDto> getTasksByFilter(@RequestBody TaskFilterDto filter) {
        if (filter == null ||
                (filter.getAssignee() == null && CollectionUtils.isEmpty(filter.getStatuses()))) {
            return Flux.error(new BadRequestException("Filter must be specified."));
        }
        return taskService.findByFilter(filter)
                .map(taskToDtoConverter::toDto);
    }

    @PutMapping(value = "/v1/task", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<TaskDto> createTask(@RequestHeader(YATT_CREATE_TASK_UID) @NotEmpty String createTaskUid,
                                    @RequestBody CreateTaskDto taskDto) {
        return taskService.create(createTaskUid, taskDto)
                .map(taskToDtoConverter::toDto);
    }

    @PostMapping(value = "/v1/task", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<TaskDto> changeTask(@RequestBody TaskDto taskDto) {
        return taskService.changeTask(taskDto)
                .map(taskToDtoConverter::toDto);
    }

    @PostMapping(value = "/v1/task/{taskId}/status/{statusId}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<TaskDto> changeStatus(@PathVariable("taskId") long taskId, @PathVariable("statusId") long statusId) {
        return taskService.changeStatus(taskId, statusId)
                .map(taskToDtoConverter::toDto);
    }

    @PostMapping(value = "/v1/task/{taskId}/assignee/{assigneeId}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<TaskDto> changeAssignee(
            @PathVariable("taskId") long taskId,
            @PathVariable(value = "assigneeId", required = false) Long assigneeId
    ) {
        return taskService.changeAssignee(taskId, assigneeId)
                .map(taskToDtoConverter::toDto);
    }

    @DeleteMapping("/v1/task/{taskId}")
    public Mono<Void> deleteTask(@PathVariable("taskId") long taskId) {
        return taskService.delete(taskId).then();
    }
}
