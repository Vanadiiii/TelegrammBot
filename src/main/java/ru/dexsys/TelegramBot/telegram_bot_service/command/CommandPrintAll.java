package ru.dexsys.TelegramBot.telegram_bot_service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.dexsys.TelegramBot.telegram_bot_service.Command;
import ru.dexsys.TelegramBot.telegram_bot_service.core.CoreService;
import ru.dexsys.TelegramBot.telegram_bot_service.core.Valute;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommandPrintAll extends AbstractCommand {
    public CommandPrintAll(CoreService coreService) {
        super(Command.PRINT_ALL, coreService);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("User " + user.getId() + " use command " + getCommandIdentifier());

        if (LocalDate.now().isAfter(coreService.getLocalDate())) {
            coreService.enrichRates();
        }

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        message.setText(
                coreService.getValuteList()
                        .stream()
                        .map(Valute::toString)
                        .collect(Collectors.joining("\n"))
        );

        execute(absSender, message, user);
    }
}
