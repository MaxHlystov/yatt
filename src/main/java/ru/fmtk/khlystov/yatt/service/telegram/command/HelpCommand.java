package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Component
public class HelpCommand extends ServiceCommand {

    public HelpCommand() {
        super("help", "Помощь");
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               Consumer<String> sender) {
        sender.accept("""
                Этот бот предназначен для управления задачами

                ❗*Список команд*
                /start - зарегистрироваться в системе
                /statuses - вывести список статусов
                /list_free - вывести список задач без ответственного
                /task - показать подробности задачи
                /tasks - вывести список ваших задач в выбранном статусе
                /take - взять свободную задачу
                /create - создать задачу, параметры: "имя задачи" ["описание задачи"]
                /status - изменить статус задачи
                /help - помощь
                """);
    }
}
