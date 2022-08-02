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
public class ShowTaskCommand extends ServiceCommand {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TaskService taskService;

    public ShowTaskCommand(TaskService taskService) {
        super("task", "Показать информацию о задаче");
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

        try {
            final long taskId = Long.parseLong(arguments[0]);
            String taskDescr = taskService.findById(taskId)
                    .map(task -> "Задача №" + task.getId() + " '" + task.getName() + "'\n" +
                            task.getDescription() + "\n" +
                            "Создана " + task.getCreatedAt().format(DATE_FORMATTER) +
                            " " + task.getCreatedBy().getName() +
                            (task.getAssignee() == null ? "" : "\nОтветственный: " + task.getAssignee().getName()))
                    .block();
            if (StringUtils.isBlank(taskDescr)) {
                taskDescr = "Задачи с номером " + taskId + " не существует.";
            }
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, taskDescr);
        } catch (Exception e) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Ошибка получения описания задачи.");
            log.error("Error when getting free list", e);
        }
    }
}
