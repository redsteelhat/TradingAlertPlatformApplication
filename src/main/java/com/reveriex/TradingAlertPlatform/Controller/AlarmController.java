package com.reveriex.TradingAlertPlatform.Controller;

import com.reveriex.TradingAlertPlatform.Services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AlarmController {
    private final AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping("/api/alarms")
    public List<Map<String, Object>> getAlarms() {
        return alarmService.getAllAlarms();
    }
}
