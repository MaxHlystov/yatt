package ru.fmtk.khlystov.yatt.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.Task;
import ru.fmtk.khlystov.yatt.dto.CreateTaskDto;
import ru.fmtk.khlystov.yatt.dto.TaskDto;
import ru.fmtk.khlystov.yatt.dto.TaskFilterDto;
import ru.fmtk.khlystov.yatt.exception.BadRequestException;
import ru.fmtk.khlystov.yatt.repository.StatusRepository;
import ru.fmtk.khlystov.yatt.repository.TaskRepository;
import ru.fmtk.khlystov.yatt.repository.UserRepository;
import ru.fmtk.khlystov.yatt.service.converter.TaskToDtoConverter;

@Service
public class TaskService {
    private final IdempotenceService idempotenceService;
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final TaskToDtoConverter taskToDtoConverter;
    private final TimeService timeService;

    public TaskService(IdempotenceService idempotenceService,
                       TaskRepository taskRepository,
                       StatusRepository statusRepository,
                       UserRepository userRepository,
                       TaskToDtoConverter taskToDtoConverter,
                       TimeService timeService) {
        this.idempotenceService = idempotenceService;
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
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

    @Transactional
    public Mono<Task> create(@NonNull String createTaskUid, @NonNull CreateTaskDto taskDto) {
        if (StringUtils.isBlank(createTaskUid)) {
            return Mono.error(new BadRequestException("You have to specify create task uid to distinct different" +
                    " creations"));
        }
        return idempotenceService.isExistsOrStore(createTaskUid)
                .flatMap(isExists -> {
                    if (isExists) {
                        return Mono.error(new BadRequestException("Creation with uid " + createTaskUid +
                                " already exists."));
                    }
                    return userRepository.findUserByName(taskDto.getCreatorName())
                            .switchIfEmpty(Mono.error(new BadRequestException("User with name " +
                                    taskDto.getCreatorName() + " was not found.")))
                            .flatMap(user -> taskRepository.save(
                                    Task.builder()
                                            .name(taskDto.getName())
                                            .statusId(1L)
                                            .description(taskDto.getDescription())
                                            .createdAt(timeService.getDateTime())
                                            .createdBy(user.getId())
                                            .build()));
                });
    }

    public Mono<Void> delete(long taskId) {
        return taskRepository.deleteById(taskId);
    }

    @Transactional
    public Mono<Task> changeTask(TaskDto taskDto) {
        if (taskDto.getId() == null) {
            return Mono.error(new BadRequestException("To change task you need to specify id."));
        }
        return taskRepository.findById(taskDto.getId())
                .switchIfEmpty(Mono.error(new BadRequestException(
                        "Task with id " + taskDto.getId() + " was not found.")))
                .flatMap(oldTask -> {
                    Task newTask = taskToDtoConverter.updateEntity(oldTask, taskDto);
                    return taskRepository.save(newTask);
                });

    }

    @Transactional
    public Mono<Task> changeStatus(long taskId, long statusId) {
        return taskRepository.updateStatus(taskId, statusId)
                .switchIfEmpty(Mono.error(new BadRequestException(
                        "Task with id " + taskId + " was not found.")))
                .then(taskRepository.findById(taskId));
    }

    @Transactional
    public Mono<Task> changeAssigneeByName(long taskId, @Nullable String assignee) {
        return taskRepository.setAssigneeByName(taskId, assignee)
                .switchIfEmpty(Mono.error(new BadRequestException(
                        "Task with id " + taskId + " was not found.")))
                .then(taskRepository.findById(taskId));
    }

    public Flux<Task> findAllWithoutAssignee() {
        return taskRepository.findAllByAssigneeIdIsNull();
    }

    public Flux<Task> findByUserTelegramIdAndStatus(long userTelegramId, String statusName) {
        return userRepository.findUserByTelegramId(userTelegramId)
                .zipWith(statusRepository.findByNameOrDescription(statusName, statusName))
                .flatMapMany(userAndStatus ->
                        taskRepository.findAllByAssigneeIdAndStatusId(
                                userAndStatus.getT1().getId(), userAndStatus.getT2().getId()));
    }

    public Mono<TaskDto> findById(long taskId) {
        return taskRepository.findById(taskId)
                .flatMap(taskToDtoConverter::toDto);
    }

    public Mono<Boolean> changeAssigneeByTelegramUserId(long taskId, Long userTelegramId) {
        return userRepository.findUserByTelegramId(userTelegramId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("You need to register before take a task")))
                .flatMap(user ->
                        taskRepository.setAssigneeById(taskId, user.getId())
                                .then(Mono.just(true))
                                .switchIfEmpty(Mono.just(false))
                );
    }
}
