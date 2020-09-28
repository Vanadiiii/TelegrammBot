package ru.dexsys.TelegramBot.telegram_bot_service.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Data
@Slf4j
public class CoreService {
    LocalDate localDate = LocalDate.now(); // TODO: 28/9/20 заглушка
    List<Valute> valuteList = List.of(     // TODO: 28/9/20 заглушка
            new Valute("AUD", 55.89, "Австралийский доллар"),
            new Valute("USD", 76.67, "Американский доллар"),
            new Valute("EUR", 92.243, "Евро")
    );

    public void enrichRates() { // TODO: 28/9/20 create!!
        try {
            //trying to enrich data from centralBankApi to database
            log.info("Enrich data of valute rates");
        } catch (Exception e) {
            log.error("Unexpected problem", e);
        }
    }

}
