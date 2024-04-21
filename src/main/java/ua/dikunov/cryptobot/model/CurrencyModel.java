package ua.dikunov.cryptobot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyModel {
    String name;
    String symbol;
    String price;
    String percentChange;
}
