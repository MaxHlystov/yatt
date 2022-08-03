package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fmtk.khlystov.yatt.service.TaskService;
import ru.fmtk.khlystov.yatt.service.converter.TaskToStringConverter;

@Component
@Slf4j
public class ShowTaskCommand extends ServiceCommand {

    private final TaskService taskService;
    private final TaskToStringConverter taskToStringConverter;


    public ShowTaskCommand(TaskService taskService, TaskToStringConverter taskToStringConverter) {
        super("task", "Показать информацию о задаче");
        this.taskService = taskService;
        this.taskToStringConverter = taskToStringConverter;
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               MessageAcceptor sender) {
        if (arguments == null || arguments.size() < 1 || StringUtils.isBlank(arguments.get(0))) {
            sender.error("Необходимо указать номер задачи как параметр команды");
            return;
        }
        final long taskId = Long.parseLong(arguments.get(0));
        String taskDescr = taskService.findById(taskId)
                .map(taskToStringConverter::getFullDescription)
                .block();
        if (StringUtils.isBlank(taskDescr)) {
            taskDescr = "Задачи с номером " + taskId + " не существует.";
        }
        sender.accept(taskDescr, getBaseReplyKeyboard());
    }
}
