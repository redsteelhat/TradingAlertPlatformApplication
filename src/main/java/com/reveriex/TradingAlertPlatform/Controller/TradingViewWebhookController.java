package com.reveriex.TradingAlertPlatform.Controller;

import com.reveriex.TradingAlertPlatform.Services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TradingViewWebhookController {

    private final AlarmService alarmService;

    @Autowired
    public TradingViewWebhookController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @PostMapping("/api/tradingview/alarm")
    public ResponseEntity<String> receiveAlarm(@RequestBody Map<String, Object> alarmData) {
        alarmService.addAlarm(alarmData); // Alarmı servise ekleyin
        return new ResponseEntity<>("Alarm received", HttpStatus.OK);
    }
}
