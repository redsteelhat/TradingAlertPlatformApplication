package com.reveriex.TradingAlertPlatform.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reveriex.TradingAlertPlatform.Utils.BinanceUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class BinanceService {

    @Value("${binance.api.key}")
    private String apiKey;

    @Value("${binance.api.secret}")
    private String apiSecret;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getFormattedAccountInfo() throws Exception {
        String url = "https://api.binance.com/api/v3/account";

        long timestamp = System.currentTimeMillis();
        String queryString = "timestamp=" + timestamp;

        // İmza oluşturma
        String signature = BinanceUtil.generateSignature(queryString, apiSecret);
        queryString += "&signature=" + signature;

        Request request = new Request.Builder()
                .url(url + "?" + queryString)
                .get()
                .addHeader("X-MBX-APIKEY", apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Binance API hatası");

            // Yanıtı JSON formatında işle
            JsonNode root = objectMapper.readTree(response.body().string());
            Map<String, Object> accountInfo = new HashMap<>();

            // Kullanıcı adı ve diğer hesap detayları
            accountInfo.put("username", apiKey); // Binance kullanıcı adını API Key olarak belirtiyoruz

            // Bakiyeler
            List<Map<String, String>> balances = new ArrayList<>();
            for (JsonNode balanceNode : root.path("balances")) {
                double availableBalance = balanceNode.path("free").asDouble();
                double lockedBalance = balanceNode.path("locked").asDouble();

                if (availableBalance > 0 || lockedBalance > 0) {
                    Map<String, String> balanceInfo = new HashMap<>();
                    balanceInfo.put("asset", balanceNode.path("asset").asText());
                    balanceInfo.put("free", String.valueOf(availableBalance));
                    balanceInfo.put("locked", String.valueOf(lockedBalance));
                    balances.add(balanceInfo);
                }
            }
            accountInfo.put("balances", balances);

            return accountInfo;
        }
    }
}
