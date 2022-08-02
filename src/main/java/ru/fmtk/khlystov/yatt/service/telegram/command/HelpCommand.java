package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class HelpCommand extends ServiceCommand {

    public HelpCommand() {
        super("help", "Помощь");
    }

    @Override
    public void executeCommand(AbsSender absSender, User user, String userName, Chat chat, String[] strings,
                               Consumer<String> sender) {
        sender.accept("""
                Этот бот предназначен для управления задачами

                ❗*Список команд*
                /list-free - вывести список задач без ответственного
                /task - показать подробности задачи
                /tasks - вывести список ваших задач в выбранном статусе
                /take - взять свободную задачу
                /create - создать задачу *
                /status - изменить статус задачи *
                /help - помощь
                """);
    }
}
