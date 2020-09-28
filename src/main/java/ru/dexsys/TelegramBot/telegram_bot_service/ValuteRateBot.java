package ru.dexsys.TelegramBot.telegram_bot_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dexsys.TelegramBot.telegram_bot_service.command.AbstractCommand;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class ValuteRateBot extends TelegramLongPollingCommandBot {
    @Value("${bot.name}")
    private String BOT_NAME;
    @Value("${bot.token}")
    private String BOT_TOKEN;

    private final List<AbstractCommand> commandList;

    public ValuteRateBot(DefaultBotOptions options, List<AbstractCommand> commandList) {
        super(options, false);
        this.commandList = commandList;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @PostConstruct
    public void setUpBotSettings() {
        log.info("Initializing ValuteRateBot...");
        log.info("Initializing Valute rate list...");

        registerAllCommands();

        registerUnknownCommand();
    }

    private void registerAllCommands() {
        commandList.stream()
                .peek(abstractCommand -> log.info("register command: '/" + abstractCommand.getCommand().commandName + "'..."))
                .forEach(this::register);
    }

    private void registerUnknownCommand() {
        registerDefaultAction((absSender, message) -> {
            log.info(
                    "User #{} is trying to execute unknown command '{}'",
                    message.getFrom().getId(),
                    message.getText()
            );
            SendMessage text = new SendMessage();
            text.setChatId(message.getChatId());
            text.setText(message.getText() + " command not found!");

            try {
                absSender.execute(text);
            } catch (TelegramApiException e) {
                log.error("Error while replying unknown command to user {}.", message.getFrom(), e);
            }

            commandList.stream()
                    .filter(command -> command.getCommand().getCommandName().equals("help"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("There is no '/help' command"))
                    .execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    //parsing message, which not started with '/'
    @Override
    public void processNonCommandUpdate(Update update) {
        log.info("Processing non-command update...");

        if (!update.hasMessage()) {
            log.error("Update doesn't have a body!");
            throw new IllegalStateException("Update doesn't have a body!");
        }

        //sending message back, that means - message was sent (like healthcheck))
        replyToUser(update);
    }

    private void replyToUser(Update update) {
        SendMessage answer = new SendMessage();
        Message message = update.getMessage();
        User user = message.getFrom();
        String messageText = message.getText();

        answer.setChatId(message.getChatId());
        answer.setText(messageText);
        try {
            execute(answer);
            log.info("Message '{}' of User #{} was received", messageText, user.getId());
        } catch (TelegramApiException e) {
            log.error("Message '{}' of User #{} was not received", messageText, user.getId(), e);
        }
    }
}
