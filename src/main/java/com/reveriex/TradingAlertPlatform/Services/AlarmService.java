package com.reveriex.TradingAlertPlatform.Services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlarmService {
    private final List<Map<String, Object>> alarms = new ArrayList<>();

    // Alarm ekleme metodu
    public void addAlarm(Map<String, Object> alarmData) {
        alarms.add(alarmData);
    }

    // Tüm alarmları döndürme metodu
    public List<Map<String, Object>> getAllAlarms() {
        return alarms;
    }
}
