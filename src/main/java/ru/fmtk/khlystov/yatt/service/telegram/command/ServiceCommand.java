package ru.fmtk.khlystov.yatt.service.telegram.command;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.fmtk.khlystov.yatt.service.telegram.BotUtils;

@Slf4j
public abstract class ServiceCommand extends BotCommand {

    ServiceCommand(String identifier, String description) {
        super(identifier, description);
    }

    protected abstract void executeCommand(User user, String userName, Chat chat,
                                           List<String> arguments, Consumer<String> sender);

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        final String userName = BotUtils.getUserName(user);
        final String debugId = "command '" + this.getCommandIdentifier() + "' for user " + userName +
                " (" + user.getId() + ")";
        log.debug("Start " + debugId + " with arguments: " + Arrays.toString(arguments));
        try {
            final List<String> argumentsByQuotes = BotUtils.getArgumentsByQuotes(arguments);
            log.debug("Parse arguments for command " + debugId + ": " +
                    argumentsByQuotes.stream()
                            .map(arg -> "\"" + arg + "\"")
                            .collect(Collectors.joining(", ")));
            executeCommand(user, userName, chat,
                    argumentsByQuotes,
                    (answer) -> sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, answer));
        } catch (Exception e) {
            log.error("Error when execute " + debugId, e);
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "К сожалению в процессе выполнения команды " + this.getCommandIdentifier() +
                            " возникла ошибка.\nЕсли Вам нужна помощь, нажмите /help");
        }
        log.debug("End " + debugId);
    }

    protected void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error when command " + commandName + " execute for user " + userName + ": " + e.getMessage(), e);
        }
    }
}
