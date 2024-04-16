package ua.dikunov.cryptobot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {

    @Value("${bot.name}")
    public String botName;
    @Value("${bot.token}")
    public String botToken;

}
