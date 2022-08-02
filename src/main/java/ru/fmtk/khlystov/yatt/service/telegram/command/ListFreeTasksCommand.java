package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.fmtk.khlystov.yatt.service.TaskService;

@Slf4j
@Component
public class ListFreeTasksCommand extends ServiceCommand {

    private final TaskService taskService;

    public ListFreeTasksCommand(TaskService taskService) {
        super("list-free", "Получить список задач без отвественного");
        this.taskService = taskService;
    }

    @Override
    public void executeCommand(AbsSender absSender, User user, String userName, Chat chat, String[] strings,
                               Consumer<String> sender) {
        List<String> tasks = taskService.findAllWithoutAssignee()
                .map(task -> "Задача №" + task.getId() + "'" + task.getName() + "'")
                .collectList().block();
        if (CollectionUtils.isEmpty(tasks)) {
            sender.accept("Список задач пуст.");
        } else {
            sender.accept("Список задач без ответственного:\n" +
                    String.join("\n", tasks) +
                    "Подробности задачи можно посмотреть командой /task");
        }
    }
}
