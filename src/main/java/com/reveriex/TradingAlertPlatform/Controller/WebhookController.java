package com.reveriex.TradingAlertPlatform.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reveriex.TradingAlertPlatform.DTO.AlertDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    private final ObjectMapper objectMapper;

    public WebhookController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        try {
            AlertDTO alert = objectMapper.readValue(payload, AlertDTO.class);
            System.out.println("Received alert: " + alert.getSymbol() + " at price " + alert.getPrice() + " to " + alert.getSide());

            // Burada Binance işlemlerini gerçekleştirin

            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid payload");
        }
    }
}
