package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fmtk.khlystov.yatt.service.TaskService;
import ru.fmtk.khlystov.yatt.service.converter.TaskToStringConverter;

@Slf4j
@Component
public class ListUserTasksCommand extends ServiceCommand {

    private final TaskService taskService;
    private final TaskToStringConverter taskToStringConverter;

    public ListUserTasksCommand(TaskService taskService, TaskToStringConverter taskToStringConverter) {
        super("tasks", "Получить список назначенных вам задач");
        this.taskService = taskService;
        this.taskToStringConverter = taskToStringConverter;
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               MessageAcceptor sender) {
        if (arguments == null || arguments.size() < 1 || StringUtils.isBlank(arguments.get(0))) {
            sender.error("Необходимо указать статус задачи как параметр команды");
            return;
        }
        final String statusName = arguments.get(0);
        List<String> tasks = taskService.findByUserTelegramIdAndStatus(user.getId(), statusName)
                .map(taskToStringConverter::headerString)
                .collectList().block();
        if (CollectionUtils.isEmpty(tasks)) {
            sender.accept("Вам не назначено ни одной задачи.\n" +
                    "Используйте команду /take чтобы взять свободную задачу в работу.");
        } else {
            sender.accept("Список назначенных вам задач:\n" +
                    String.join("\n", tasks) +
                    "\nПодробности задачи можно посмотреть командой /task");
        }
    }
}
