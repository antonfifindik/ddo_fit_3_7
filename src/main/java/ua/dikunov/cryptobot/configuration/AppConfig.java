package ua.dikunov.cryptobot.configuration;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Data
public class AppConfig {

    private final ApiConfig apiConfig;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiConfig.apiUrl)
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.set("Content-Type", "application/json");
                            httpHeaders.set("X-CMC_PRO_API_KEY", apiConfig.botKey);
                        })
                .build();
    }
}
