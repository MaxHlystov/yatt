package ru.fmtk.khlystov.yatt.service.telegram.command;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.fmtk.khlystov.yatt.service.telegram.BotCommandsMetaStore;
import ru.fmtk.khlystov.yatt.service.telegram.keyboard.BaseKeyboardCreator;

public abstract class ServiceCommandWithKeyboard extends ServiceCommand {

    private final BaseKeyboardCreator baseKeyboardCreator;

    public ServiceCommandWithKeyboard(String identifier,
                                      String description,
                                      BaseKeyboardCreator baseKeyboardCreator) {
        super(identifier, description);
        this.baseKeyboardCreator = baseKeyboardCreator;
    }

    protected BaseKeyboardCreator getBaseKeyboardCreator() {
        return baseKeyboardCreator;
    }

    protected ReplyKeyboard getBaseReplyKeyboard() {
        return getBaseKeyboardCreator().getMenuKeyboard(BotCommandsMetaStore.getNewUserCommands());
    }
}
