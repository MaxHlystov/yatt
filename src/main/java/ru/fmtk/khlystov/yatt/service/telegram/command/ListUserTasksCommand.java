package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.fmtk.khlystov.yatt.service.TaskService;

@Slf4j
@Component
public class ListUserTasksCommand extends ServiceCommand {

    private final TaskService taskService;

    public ListUserTasksCommand(TaskService taskService) {
        super("tasks", "Получить список назначенных вам задач");
        this.taskService = taskService;
    }

    @Override
    public void executeCommand(AbsSender absSender, User user, String userName, Chat chat, String[] arguments,
                               Consumer<String> sender) {
        if (arguments == null || arguments.length < 1 || StringUtils.isBlank(arguments[0])) {
            sender.accept("Необходимо указать статус задачи как параметр команды");
            return;
        }
        final String statusName = arguments[0];
        List<String> tasks = taskService.findByUserTelegramIdAndStatus(user.getId(), statusName)
                .map(task -> "Задача №" + task.getId() + "'" + task.getName() + "'")
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
