package ru.dexsys.TelegramBot.telegram_bot_service.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Valute {
    @Id
    String name;
    double value;
    String description;

    @Override
    public String toString() {
        return name + ": " + value + "₽ (" + description + ")";
    }
}
