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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BinanceService {

    @Value("${binance.api.key}")
    private String apiKey;

    @Value("${binance.api.secret}")
    private String apiSecret;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Spot Bakiyesini Getir
    public List<Map<String, String>> getSpotBalance() throws Exception {
        String url = "https://api.binance.com/api/v3/account";
        long timestamp = System.currentTimeMillis();
        String queryString = "timestamp=" + timestamp;

        String signature = BinanceUtil.generateSignature(queryString, apiSecret);
        queryString += "&signature=" + signature;

        Request request = new Request.Builder()
                .url(url + "?" + queryString)
                .get()
                .addHeader("X-MBX-APIKEY", apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Binance Spot API hatası");

            JsonNode root = objectMapper.readTree(response.body().string());
            List<Map<String, String>> spotBalances = new ArrayList<>();

            for (JsonNode balanceNode : root.path("balances")) {
                double availableBalance = balanceNode.path("free").asDouble();
                double lockedBalance = balanceNode.path("locked").asDouble();

                if (availableBalance > 0 || lockedBalance > 0) {
                    Map<String, String> balanceInfo = new HashMap<>();
                    balanceInfo.put("asset", balanceNode.path("asset").asText());
                    balanceInfo.put("free", String.valueOf(availableBalance));
                    balanceInfo.put("locked", String.valueOf(lockedBalance));
                    spotBalances.add(balanceInfo);
                }
            }
            return spotBalances;
        }
    }

    // Futures Bakiyesini Getir
    public List<Map<String, String>> getFuturesBalance() throws Exception {
        String url = "https://fapi.binance.com/fapi/v2/account";
        long timestamp = System.currentTimeMillis();
        String queryString = "timestamp=" + timestamp;

        String signature = BinanceUtil.generateSignature(queryString, apiSecret);
        queryString += "&signature=" + signature;

        Request request = new Request.Builder()
                .url(url + "?" + queryString)
                .get()
                .addHeader("X-MBX-APIKEY", apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Binance Futures API hatası");

            JsonNode root = objectMapper.readTree(response.body().string());
            List<Map<String, String>> futuresBalances = new ArrayList<>();

            for (JsonNode assetNode : root.path("assets")) {
                double availableBalance = assetNode.path("availableBalance").asDouble();
                double walletBalance = assetNode.path("walletBalance").asDouble();

                if (walletBalance > 0) {
                    Map<String, String> balanceInfo = new HashMap<>();
                    balanceInfo.put("asset", assetNode.path("asset").asText());
                    balanceInfo.put("availableBalance", String.valueOf(availableBalance));
                    balanceInfo.put("walletBalance", String.valueOf(walletBalance));
                    futuresBalances.add(balanceInfo);
                }
            }
            return futuresBalances;
        }
    }
}
