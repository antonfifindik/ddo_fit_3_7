package ua.dikunov.cryptobot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApiConfig {

    @Value("${api.url}")
    public String apiUrl;
    @Value("${api.key}")
    public String botKey;

}
