package ru.fmtk.khlystov.yatt.service.telegram;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramWebhookCommandBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramWebhookCommandBot {
    @Override
    public String getBotToken() {
        return null;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotUsername() {
        return null;
    }
}
