package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fmtk.khlystov.yatt.service.telegram.keyboard.BaseKeyboardCreator;

@Slf4j
@Component
public class HelpCommand extends ServiceCommandWithKeyboard {

    public HelpCommand(BaseKeyboardCreator baseKeyboardCreator) {
        super("help", "Помощь", baseKeyboardCreator);
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               MessageAcceptor sender) {
        sender.accept("""
                Этот бот предназначен для управления задачами

                ❗*Список команд*
                /start - зарегистрироваться в системе
                /statuses - вывести список статусов
                /freetasks - вывести список задач без ответственного
                /task - показать подробности задачи
                /tasks - вывести список ваших задач в выбранном статусе
                /take - взять свободную задачу
                /create - создать задачу, параметры: "имя задачи" ["описание задачи"]
                /status - изменить статус задачи
                /help - помощь
                """);
    }
}
