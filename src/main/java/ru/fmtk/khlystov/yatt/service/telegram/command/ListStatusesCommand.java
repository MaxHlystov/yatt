package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fmtk.khlystov.yatt.domain.Status;
import ru.fmtk.khlystov.yatt.repository.StatusRepository;
import ru.fmtk.khlystov.yatt.service.converter.TaskToStringConverter;

@Component
public class ListStatusesCommand extends ServiceCommand {

    private final StatusRepository statusRepository;

    public ListStatusesCommand(StatusRepository statusRepository,
                               TaskToStringConverter taskToStringConverter
    ) {
        super("statuses", "Получить список статусов");
        this.statusRepository = statusRepository;
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               Consumer<String> sender) {
        List<String> statuses = statusRepository.findAll()
                .map(ListStatusesCommand::getStatusDescr)
                .collectList()
                .block();
        if (CollectionUtils.isEmpty(statuses)) {
            sender.accept("Список статусов пуст :(");
        } else {
            sender.accept("Список статусов (код, имя):\n" +
                    String.join("\n", statuses) +
                    "\nВ других командах можно использовать код или имя статуса в кавычках.");
        }
    }

    private static String getStatusDescr(Status status) {
        if (StringUtils.isBlank(status.getDescription())) {
            return status.getName();
        }
        return status.getName() + ", \"" + status.getDescription() + "\"";
    }
}
