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
                    var top10Message = new StringBuilder();
                    for (CurrencyModel currency : currencyService.getTop10CurrencyRate()) {
                        top10Message.append(currency.getSymbol()).append(" (").append(currency.getName()).append(")").append("\n")
                                .append("\n").append(currency.getPrice()).append(" usd")
                                .append("\n24h change: ").append(currency.getPercentChange()).append("%")
                                .append("\n-----------------------------\n\n");
                    }
                    sendMessage(chatId, top10Message.toString());
                    break;
                default:
                    var message = new StringBuilder();
                    CurrencyModel currency = currencyService.getPrice(messageText);
                    message.append(currency.getSymbol()).append(" (").append(currency.getName()).append(")").append("\n")
                            .append("\n").append(currency.getPrice()).append(" usd")
                            .append("\n24h change: ").append(currency.getPercentChange()).append("%");
                    sendMessage(chatId, message.toString());
                    break;
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n\n" +
                "Available commands:\n" +
                "/price - get current prices for the top 10 cryptocurrencies\n\n" +
                "Or write the name of the required cryptocurrency. For example: near";
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
