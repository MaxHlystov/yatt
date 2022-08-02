package ru.fmtk.khlystov.yatt.service.telegram;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.fmtk.khlystov.yatt.service.telegram.command.NonCommand;
import ru.fmtk.khlystov.yatt.service.telegram.command.ServiceCommand;

@Slf4j
@Service
public class Bot extends TelegramLongPollingCommandBot {

    private final String botName;
    private final String botToken;

    private final NonCommand nonCommand;

    public Bot(@Value("${yatt.bot.name}") String botName,
               @Value("${yatt.bot.token}") String botToken,
               NonCommand nonCommand,
               List<ServiceCommand> commands) {
        super();

        this.botName = botName;
        this.botToken = botToken;
        this.nonCommand = nonCommand;

        log.debug("Start to register commands: " + commands.stream()
                .map(ServiceCommand::getCommandIdentifier)
                .collect(Collectors.joining(", ")));
        commands.forEach(this::register);
        log.debug("Bot started");
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = BotUtils.getUserName(msg);

        String answer = nonCommand.nonCommandExecute(chatId, userName, msg.getText());
        setAnswer(chatId, userName, answer);
    }

    private void setAnswer(long chatId, @NonNull String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error("Telegram error for user " + userName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void register(@NonNull BotCommand botCommand) {
        super.register(botCommand);
        log.debug("Registered command " + botCommand);
    }
}
