package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fmtk.khlystov.yatt.service.TaskService;
import ru.fmtk.khlystov.yatt.service.converter.TaskToStringConverter;

@Slf4j
@Component
public class ListFreeTasksCommand extends ServiceCommand {

    private final TaskService taskService;
    private final TaskToStringConverter taskToStringConverter;

    public ListFreeTasksCommand(TaskService taskService, TaskToStringConverter taskToStringConverter) {
        super("list_free", "Получить список задач без ответственного");
        this.taskService = taskService;
        this.taskToStringConverter = taskToStringConverter;
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               Consumer<String> sender) {
        List<String> tasks = taskService.findAllWithoutAssignee()
                .map(taskToStringConverter::headerString)
                .collectList().block();
        if (CollectionUtils.isEmpty(tasks)) {
            sender.accept("Список задач пуст.");
        } else {
            sender.accept("Список задач без ответственного:\n" +
                    String.join("\n", tasks) +
                    "\nПодробности задачи можно посмотреть командой /task");
        }
    }
}
