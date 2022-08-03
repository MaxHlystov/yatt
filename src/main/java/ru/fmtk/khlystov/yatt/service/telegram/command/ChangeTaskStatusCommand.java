package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fmtk.khlystov.yatt.service.TaskService;
import ru.fmtk.khlystov.yatt.service.converter.TaskToStringConverter;

@BotCommandMetadata(identifyer = "status", description = "Изменить статус задачи")
public class ChangeTaskStatusCommand extends ServiceCommand {

    private final TaskService taskService;
    private final TaskToStringConverter taskToStringConverter;

    public ChangeTaskStatusCommand(TaskService taskService, TaskToStringConverter taskToStringConverter) {
        super("status", "Изменить статус задачи");
        this.taskService = taskService;
        this.taskToStringConverter = taskToStringConverter;
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               MessageAcceptor sender) {
        if (arguments == null || arguments.size() < 2 || StringUtils.isBlank(arguments.get(0))
                || StringUtils.isBlank(arguments.get(1))) {
            sender.error("Необходимо указать номер задачи и имя статуса как параметры команды");
            return;
        }
        final long taskId = Long.parseLong(arguments.get(0));
        final String statusName = arguments.get(1);
        String taskDescr = taskService.changeStatusByName(taskId, statusName)
                .flatMap(taskToStringConverter::getFullDescription)
                .block();
        sender.accept("Статус задачи № " + taskId + " успешно изменен.\n" + taskDescr);
    }
}
