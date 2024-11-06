package com.reveriex.TradingAlertPlatform.Controller;

import com.reveriex.TradingAlertPlatform.Services.AlarmService;
import com.reveriex.TradingAlertPlatform.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final UserService userService;
    private final AlarmService alarmService;

    @Autowired
    public HomeController(UserService userService, AlarmService alarmService) {
        this.userService = userService;
        this.alarmService = alarmService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Giriş yapan kullanıcı adı
        String username = userService.getLoggedInUsername();
        model.addAttribute("loggedInUsername", username);

        // Alarmları modelde ekleme
        List<Map<String, Object>> alarms = alarmService.getAllAlarms();
        model.addAttribute("alarms", alarms);

        return "index"; // Thymeleaf template adı
    }
}
