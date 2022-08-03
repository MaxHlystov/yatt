package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fmtk.khlystov.yatt.service.UserService;

@Slf4j
@Component
public class StartCommand extends ServiceCommand {

    private final UserService userService;

    public StartCommand(UserService userService) {
        super("start", "Старт");
        this.userService = userService;
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               Consumer<String> sender) {
        if (user.getIsBot()) {
            sender.accept("Бот не может быть зарегистрирован в системе.");
        }
        ru.fmtk.khlystov.yatt.domain.User domainUser =
                userService.findOrCreateUserByTelegramId(user.getId(), userName)
                        .block();
        sender.accept(domainUser == null
                ? "Добро пожаловать в таск-трекер! К сожалению не получилось вас зарегистрировать.\n" +
                "Если Вам нужна помощь, нажмите /help"
                : "Добро пожаловать в таск-трекер! Вы успешно зарегистрированы под именем: " +
                domainUser.getName() + ". Если Вам нужна помощь, нажмите /help");
    }
}
