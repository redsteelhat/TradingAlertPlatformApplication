package com.reveriex.TradingAlertPlatform.Controller;

import com.reveriex.TradingAlertPlatform.Services.BinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequestMapping("/binance")
public class BinanceController {

    private final BinanceService binanceService;

    @Autowired
    public BinanceController(BinanceService binanceService) {
        this.binanceService = binanceService;
    }

    @GetMapping("/account")
    public String getAccountInfo(Model model) {
        try {
            Map<String, Object> accountInfo = binanceService.getFormattedAccountInfo();
            model.addAttribute("accountInfo", accountInfo);
        } catch (Exception e) {
            model.addAttribute("error", "Hesap bilgileri alınamadı: " + e.getMessage());
        }
        return "account";
    }
}
