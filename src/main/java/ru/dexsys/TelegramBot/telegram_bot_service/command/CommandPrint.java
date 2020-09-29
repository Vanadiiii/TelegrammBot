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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommandPrint extends AbstractCommand {
    public CommandPrint(CoreService coreService) {
        super(Command.PRINT, coreService);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) throws RuntimeException {
        if (LocalDate.now().isAfter(coreService.getLocalDate())) {
            coreService.enrichRates();
        }

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());
        StringBuilder stringBuilder = new StringBuilder();
        List<Valute> valuteList = coreService.getValuteList();

        if (arguments.length == 1) {
            stringBuilder.append(
                    valuteList.stream()
                            .filter(valute -> valute.getName().equals(arguments[0]))
                            .map(Valute::toString)
                            .collect(Collectors.joining("\n"))
            );
        } else if (arguments.length == 2) {
            Valute firstValute = findValuteByName(valuteList, 0, arguments);
            Valute secondValute = findValuteByName(valuteList, 1, arguments);
            stringBuilder.append("Course: ")
                    .append(firstValute.getName())
                    .append(" to ")
                    .append(secondValute.getName())
                    .append(" is ")
                    .append(firstValute.getValue() / secondValute.getValue())
                    .append(" ")
                    .append(firstValute.getName());
        } else if (arguments.length > 2) {
            log.error("too many arguments for method '/print'");
            stringBuilder.append("too many arguments for method '/print'");
        } else {
            log.error("no arguments for method '/print'");
            stringBuilder.append("no arguments for method '/print'");
        }

        message.setText(stringBuilder.toString());

        execute(absSender, message, user);
    }

    private Valute findValuteByName(List<Valute> valuteList, int i, String[] arguments) {
        return valuteList.stream()
                .filter(valute -> valute.getName().equals(arguments[i]))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("unknown type of valute");
                    throw new RuntimeException("unknown type of valute");
                });
    }
}
