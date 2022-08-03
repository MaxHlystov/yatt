package ru.fmtk.khlystov.yatt.service.telegram.keyboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Service
public class BaseKeyboardCreator {

    public static final int TWO_COLUMNS_COUNT = 2;

    public ReplyKeyboardMarkup getMenuKeyboard(final List<String> commands) {
        return getMenuKeyboard(commands, TWO_COLUMNS_COUNT);
    }

    public ReplyKeyboardMarkup getMenuKeyboard(final List<String> commands, int columns) {
        if (columns < 1 || columns > 5) {
            columns = 3;
        }
        List<KeyboardRow> keyboard = new ArrayList<>(commands.size() / columns + 1);
        for (int cmdIdx = 0; cmdIdx < commands.size(); ) {
            final KeyboardRow keyboardRow = new KeyboardRow();
            for (int col = 0; col < columns && cmdIdx < commands.size(); ++col, ++cmdIdx) {
                keyboardRow.add(new KeyboardButton(commands.get(cmdIdx)));
            }
            if (!keyboardRow.isEmpty()) {
                keyboard.add(keyboardRow);
            }
        }

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

}
