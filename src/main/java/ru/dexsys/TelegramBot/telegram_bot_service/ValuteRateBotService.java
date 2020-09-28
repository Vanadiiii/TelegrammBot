package ru.dexsys.TelegramBot.telegram_bot_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValuteRateBotService {
    @Value("${bot.name}")
    private String botName;

    private final TelegramLongPollingCommandBot bot;
    private final TelegramBotsApi botsApi = new TelegramBotsApi();

    @PostConstruct
    public void run() {
        try {
            ApiContextInitializer.init();
            log.info("Registering " + botName + "...");
            botsApi.registerBot(bot);
            log.info("Valute rate bot is ready for work!");
        } catch (TelegramApiRequestException e) {
            log.error("Error while initializing bot!", e);
        }
    }
}
