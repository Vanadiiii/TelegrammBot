package ru.dexsys.TelegramBot.telegram_bot_service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.dexsys.TelegramBot.telegram_bot_service.Command;
import ru.dexsys.TelegramBot.telegram_bot_service.core.CoreService;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommandHelp extends AbstractCommand {
    public CommandHelp(CoreService coreService) {
        super(Command.HELP, coreService);

    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) throws RuntimeException {
        String text = EnumSet.allOf(Command.class)
                .stream()
                .map(Enum::toString)
                .map(StringBuilder::new)
                .collect(Collectors.joining("\n", "<b>Available commands:</b>\n", ""));

        SendMessage helpMessage = new SendMessage();
        helpMessage.setChatId(chat.getId().toString());
        helpMessage.enableHtml(true);
        helpMessage.setText(text);

        execute(absSender, helpMessage, user);
    }
}
