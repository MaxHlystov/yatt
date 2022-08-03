package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

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

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
                               Consumer<String> sender) {
        if (arguments == null || arguments.size() < 2 || StringUtils.isBlank(arguments.get(0))) {
            sender.accept("Необходимо указать название задачи как параметры команды");
            return;
        }
        final String taskName = arguments.get(0);
        final String description = arguments.get(1);
        final String uid = generateCreateUid(user.getId());
        final CreateTaskDto taskDto = new CreateTaskDto(taskName, userName, description);
        String taskDescr = taskService.createFromTelegram(uid, user.getId(), taskDto)
                .flatMap(taskToStringConverter::getFullDescription)
                .block();
        sender.accept("Задача успешно создана:\n" + taskDescr);
    }

    private String generateCreateUid(long userId) {
        return "Tb_" + userId + "_" + timeService.getDateTime().format(DATE_TIME_FORMATTER);
    }
}
