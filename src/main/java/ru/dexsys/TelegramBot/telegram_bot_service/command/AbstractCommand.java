package ru.dexsys.TelegramBot.telegram_bot_service.command;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dexsys.TelegramBot.telegram_bot_service.Command;
import ru.dexsys.TelegramBot.telegram_bot_service.core.CoreService;

@Slf4j
@Getter
public abstract class AbstractCommand extends BotCommand {
    private final Command command;
    protected final CoreService coreService;

    public AbstractCommand(Command command, CoreService coreService) {
        super(command.getCommandName(), command.getDescription());
        this.command = command;
        this.coreService = coreService;
    }

    public void execute(AbsSender sender, SendMessage message, User user) {
        log.info("User " + user.getId() + " use command " + getCommandIdentifier());
        try {
            sender.execute(message);
            log.info("SUCCESS -> User #" + user.getId() + " execute command " + getCommandIdentifier());
        } catch (TelegramApiException e) {
            log.error("EXCEPTION -> User #" + user.getId() + "can't execute command " + getCommandIdentifier());
        }
    }
}
