package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fmtk.khlystov.yatt.dto.CreateTaskDto;
import ru.fmtk.khlystov.yatt.service.TaskService;
import ru.fmtk.khlystov.yatt.service.TimeService;
import ru.fmtk.khlystov.yatt.service.converter.TaskToStringConverter;

@Component
public class CreateTaskCommand extends ServiceCommand {

    private static final int UID_MAX_LENGTH = 100;
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private final TaskService taskService;
    private final TimeService timeService;
    private final TaskToStringConverter taskToStringConverter;

    public CreateTaskCommand(TaskService taskService,
                             TimeService timeService,
                             TaskToStringConverter taskToStringConverter) {
        super("create", "Создать задачу");
        this.taskService = taskService;
        this.timeService = timeService;
        this.taskToStringConverter = taskToStringConverter;
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               MessageAcceptor sender) {
        if (arguments == null || arguments.size() < 1 || StringUtils.isBlank(arguments.get(0))) {
            sender.error("Необходимо указать название задачи как параметры команды");
            return;
        }
        final String taskName = arguments.get(0);
        final String description = getDescription(arguments);
        final String uid = generateCreateUid(user.getId(), taskName);
        final CreateTaskDto taskDto = new CreateTaskDto(taskName, userName, description);
        String taskDescr = taskService.createFromTelegram(uid, user.getId(), taskDto)
                .flatMap(taskToStringConverter::getFullDescription)
                .block();
        sender.accept("Задача успешно создана:\n" + taskDescr);
    }

    private static String getDescription(List<String> arguments) {
        if (arguments.size() < 2) {
            return null;
        }
        return arguments.get(1);
    }

    private String generateCreateUid(long userId, String taskName) {
        return StringUtils.left(
                userId + "_" + timeService.getDateTime().format(DATE_TIME_FORMATTER) + taskName,
                UID_MAX_LENGTH);
    }
}
