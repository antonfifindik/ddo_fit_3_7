package ua.dikunov.cryptobot.service;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ua.dikunov.cryptobot.model.CurrencyModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Data
public class CurrencyService {
    private final RestClient restClient;

    public List<CurrencyModel> getTop10CurrencyRate() {
        JSONObject jsonResponse = new JSONObject(restClient.get().uri("/listings/latest?start=1&limit=10&convert=USD").retrieve().body(String.class));
        JSONArray jsonArray = jsonResponse.getJSONArray("data");
        var resultList = new ArrayList<CurrencyModel>();

        IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .forEach(jsonObject -> {
                    resultList.add(new CurrencyModel(jsonObject.getString("name"),
                        jsonObject.getString("symbol"), new DecimalFormat("#.##").format(jsonObject.getJSONObject("quote")
                            .getJSONObject("USD").getDouble("price")),
                            new DecimalFormat("#.##").format(jsonObject.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h"))));
                });

        return resultList;
    }

    public CurrencyModel getPrice(String symbol) {
        JSONObject jsonResponse = new JSONObject(restClient.get().uri("/quotes/latest?symbol=" + symbol).retrieve().body(String.class));
        JSONObject jsonObject = jsonResponse.getJSONObject("data").getJSONObject(symbol.toUpperCase());
        return new CurrencyModel(jsonObject.getString("name"), jsonObject.getString("symbol"), new DecimalFormat("#.##").format(jsonObject.getJSONObject("quote")
                .getJSONObject("USD").getDouble("price")),
                new DecimalFormat("#.##").format(jsonObject.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h")));
    }

}
