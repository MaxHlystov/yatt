package ru.fmtk.khlystov.yatt.service.telegram.command;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface MessageAcceptor {
    void accept(String message);

    void accept(String message, ReplyKeyboard replyKeyboard);

    void error(String message);
}
