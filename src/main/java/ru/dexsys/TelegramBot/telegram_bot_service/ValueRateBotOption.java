package ru.dexsys.TelegramBot.telegram_bot_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ValueRateBotOption extends DefaultBotOptions {
    @Value("${proxy.host}")
    String proxyHost;
    @Value("${proxy.port}")
    int proxyPort;
    @Value("${proxy.type}")
    ProxyType proxyType;

    @PostConstruct
    public void initializeOptions() {
        System.getProperty("proxySet", "true");
        System.getProperty("socksProxyHost", proxyHost);
        System.getProperty("socksProxyPort", String.valueOf(proxyPort));

        log.info("Initializing API context...");
        ApiContextInitializer.init();


        log.info("Configuring bot options...");
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        botOptions.setProxyHost(proxyHost);
        botOptions.setProxyPort(proxyPort);
        botOptions.setProxyType(proxyType);
    }
}
