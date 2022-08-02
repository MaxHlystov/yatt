package ru.fmtk.khlystov.yatt.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.Status;
import ru.fmtk.khlystov.yatt.domain.Task;
import ru.fmtk.khlystov.yatt.dto.CreateTaskDto;
import ru.fmtk.khlystov.yatt.dto.TaskDto;
import ru.fmtk.khlystov.yatt.dto.TaskFilterDto;
import ru.fmtk.khlystov.yatt.exception.BadRequestException;
import ru.fmtk.khlystov.yatt.repository.CreateTaskUidRepository;
import ru.fmtk.khlystov.yatt.repository.TaskRepository;
import ru.fmtk.khlystov.yatt.repository.UserRepository;
import ru.fmtk.khlystov.yatt.service.converter.TaskToDtoConverter;

@Service
public class TaskService {
    private final CreateTaskUidRepository createTaskUidRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskToDtoConverter taskToDtoConverter;
    private final TimeService timeService;

    public TaskService(CreateTaskUidRepository createTaskUidRepository,
                       TaskRepository taskRepository,
                       UserRepository userRepository,
                       TaskToDtoConverter taskToDtoConverter,
                       TimeService timeService) {
        this.createTaskUidRepository = createTaskUidRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskToDtoConverter = taskToDtoConverter;
        this.timeService = timeService;
    }

    public Flux<Task> findByFilter(TaskFilterDto filter) {
        boolean hasAssigneeFilter = StringUtils.isNotBlank(filter.getAssignee());
        boolean hasStatusesFilter = CollectionUtils.isNotEmpty(filter.getStatuses());
        if (hasAssigneeFilter && hasStatusesFilter) {
            return taskRepository.findAllByAssigneeNameAndStatusIdIn(filter.getAssignee(), filter.getStatuses());
        }
        if (hasAssigneeFilter) {
            return taskRepository.findAllByAssigneeName(filter.getAssignee());
        }
        if (hasStatusesFilter) {
            return taskRepository.findAllByStatusIdIn(filter.getStatuses());
        }
        throw new IllegalArgumentException("Filter must not be empty.");
    }

    public Mono<Task> create(@NonNull String createTaskUid, @NonNull CreateTaskDto taskDto) {
        if (StringUtils.isBlank(createTaskUid)) {
            return Mono.error(new BadRequestException("You have to specify create task uid to distinct different" +
                    " creations"));
        }
        return createTaskUidRepository.insertIfNotExists(createTaskUid, timeService.getDateTime())
                .flatMap(uid -> {
                    if (uid == null) {
                        return Mono.error(new BadRequestException("Creation with uid " + createTaskUid +
                                " already exists."));
                    }
                    return userRepository.findUserByName(taskDto.getCreatorName())
                            .flatMap(user -> taskRepository.save(
                                    Task.builder()
                                            .name(taskDto.getName())
                                            .status(new Status(1L, null))
                                            .description(taskDto.getDescription())
                                            .createdAt(timeService.getDateTime())
                                            .createdBy(user)
                                            .build()));
                });
    }

    public Mono<Void> delete(long taskId) {
        return taskRepository.deleteById(taskId);
    }

    public Mono<Task> changeTask(TaskDto taskDto) {
        if (taskDto.getId() == null) {
            return Mono.error(new BadRequestException("To change task you need to specify id."));
        }
        return taskRepository.findById(taskDto.getId())
                .flatMap(oldTask -> {
                    Task newTask = taskToDtoConverter.updateEntity(oldTask, taskDto);
                    return taskRepository.save(newTask);
                });

    }

    public Mono<Task> changeStatus(long taskId, long statusId) {
        return taskRepository.updateStatus(taskId, statusId);
    }

    public Mono<Task> changeAssignee(long taskId, @Nullable Long assigneeId) {
        return taskRepository.setAssignee(taskId, assigneeId);
    }
}
