package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
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
    public void executeCommand(AbsSender absSender, User user, String userName, Chat chat, String[] arguments,
                               Consumer<String> sender) {
        ru.fmtk.khlystov.yatt.domain.User domainUser =
                userService.findOrCreateUserByTelegramId(user.getId(), userName)
                        .block();
        sender.accept(domainUser == null
                ? "Добро пожаловать в таск-трекер! К сожалению не получилось вас зарегистировать.\n" +
                "Если Вам нужна помощь, нажмите /help"
                : "Добро пожаловать в таск-трекер! Вы успешно зарегистированы под именем: " +
                domainUser.getName() + " Если Вам нужна помощь, нажмите /help");
    }
}
