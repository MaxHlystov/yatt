package ru.fmtk.khlystov.yatt.service.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class BotUtils {
    private BotUtils() {}

    public static String getUserName(Message msg) {
        return getUserName(msg.getFrom());
    }

    public static String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }
}
