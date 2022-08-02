package ru.fmtk.khlystov.yatt.service.telegram.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NonCommand {

    public String nonCommandExecute(Long chatId, String userName, String text) {
        // Пользователь %s. Начата обработка сообщения \"%s\", не являющегося командой",
        log.debug("Non command from user {} to chat id {}: {}", userName, chatId, text);
        return """
                Простите, я не понимаю Вас. Похоже, что Вы ввели сообщение, не соответствующее формату
                Возможно, Вам поможет /help""";
    }
}
