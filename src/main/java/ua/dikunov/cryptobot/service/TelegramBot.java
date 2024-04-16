package ua.dikunov.cryptobot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.dikunov.cryptobot.configuration.BotConfig;
import ua.dikunov.cryptobot.model.CurrencyModel;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final CurrencyService currencyService;

    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.botName;
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/price":
                    var message = new StringBuilder();
                    for (CurrencyModel currency : currencyService.getCurrencyRate()) {
                        message.append(currency.getSymbol()).append(" (").append(currency.getName()).append(")").append("\n")
                                .append("\n").append(currency.getPrice()).append(" usd")
                                .append("\n-----------------------------\n\n");
                    }
                    sendMessage(chatId, message.toString());
                    break;
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n\n" +
                "Available commands:\n" +
                "/price - get current prices for the top 10 cryptocurrencies";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

}
