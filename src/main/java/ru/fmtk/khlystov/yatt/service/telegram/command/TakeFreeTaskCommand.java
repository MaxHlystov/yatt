package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.fmtk.khlystov.yatt.service.TaskService;

@Component
@Slf4j
public class TakeFreeTaskCommand extends ServiceCommand {

    private final TaskService taskService;

    public TakeFreeTaskCommand(TaskService taskService) {
        super("take", "Вять задачу в работу");
        this.taskService = taskService;
    }

    @Override
    public void executeCommand(AbsSender absSender, User user, String userName, Chat chat, String[] arguments,
                               Consumer<String> sender) {
        if (arguments == null || arguments.length < 1 || StringUtils.isBlank(arguments[0])) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Необходимо указать номер задачи как параметр команды");
            return;
        }
        final long taskId = Long.parseLong(arguments[0]);
        Boolean done = taskService.changeAssigneeByTelegramUserId(taskId, user.getId())
                .block();
        sender.accept(Boolean.TRUE.equals(done)
                ? "Задача №" + taskId + " взята в работу."
                : "Не удалось взять задачу №" + taskId + " в работу.");
    }
}
