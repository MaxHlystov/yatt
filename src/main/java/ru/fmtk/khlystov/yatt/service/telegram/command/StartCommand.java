package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.fmtk.khlystov.yatt.service.UserService;
import ru.fmtk.khlystov.yatt.service.telegram.BotCommandsMetaStore;
import ru.fmtk.khlystov.yatt.service.telegram.keyboard.BaseKeyboardCreator;

@Slf4j
@Component
public class StartCommand extends ServiceCommandWithKeyboard {

    private final UserService userService;
    private final BotCommandsMetaStore botCommandsMetaStore;

    public StartCommand(UserService userService,
                        BotCommandsMetaStore botCommandsMetaStore,
                        BaseKeyboardCreator baseKeyboardCreator) {
        super("start", "Старт", baseKeyboardCreator);
        this.userService = userService;
        this.botCommandsMetaStore = botCommandsMetaStore;
    }

    @Override
    public void executeCommand(User user, String userName, Chat chat, List<String> arguments,
                               MessageAcceptor sender) {
        if (user.getIsBot()) {
            sender.error("Бот не может быть зарегистрирован в системе.");
        }
        ru.fmtk.khlystov.yatt.domain.User domainUser =
                userService.findOrCreateUserByTelegramId(user.getId(), userName)
                        .block();
        if (domainUser == null) {
            sender.error("Добро пожаловать в таск-трекер! К сожалению не получилось вас зарегистрировать.\n" +
                    "Если Вам нужна помощь, нажмите /help");
        } else {
            sender.accept("Добро пожаловать в таск-трекер! Вы успешно зарегистрированы под именем: " +
                            domainUser.getName() + ". Если Вам нужна помощь, нажмите /help",
                    getKeyboardMarkup());
        }
    }

    protected ReplyKeyboardMarkup getKeyboardMarkup() {
        return getBaseKeyboardCreator().getMenuKeyboard(
                botCommandsMetaStore.getCommands().stream()
                        .map(cmd -> "/" + cmd)
                        .collect(Collectors.toList())
                , 3);
    }
}
