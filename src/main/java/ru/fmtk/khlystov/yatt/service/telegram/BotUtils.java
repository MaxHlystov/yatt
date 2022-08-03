package ru.fmtk.khlystov.yatt.service.telegram;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class BotUtils {
    private BotUtils() {
    }

    public static String getUserName(Message msg) {
        return getUserName(msg.getFrom());
    }

    public static String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    public static List<String> getArgumentsByQuotes(final String[] arguments) {
        final String delimiter = " ";
        List<String> result = new ArrayList<>();
        boolean isStarted = false;
        for (final String arg : arguments) {
            if (StringUtils.isNotBlank(arg)) {
                String trimmedArg = arg.trim();
                boolean isStart = trimmedArg.charAt(0) == '"';
                boolean isEnd = trimmedArg.charAt(trimmedArg.length() - 1) == '"';
                String toInsert = trimmedArg.substring(isStart ? 1 : 0,
                        isEnd ? trimmedArg.length() - 1 : trimmedArg.length());
                if(StringUtils.isNotBlank(toInsert)) {
                    if (isStarted) {
                        result.set(result.size()-1, result.get(result.size()-1) + delimiter + toInsert);
                        isStarted = !isEnd;
                    } else {
                        result.add(toInsert);
                        isStarted = isStart;
                    }
                }
            }
        }
        return result;
    }
}
