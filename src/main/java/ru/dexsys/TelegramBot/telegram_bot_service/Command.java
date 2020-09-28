package ru.dexsys.TelegramBot.telegram_bot_service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    PRINT("print", "print rate of chosen rate course"),
    PRINT_ALL("print_all", "print all courses from CentralBankRF"),
    HELP("help", "print all bot's command"),
    ;
    String commandName;
    String description;
}
