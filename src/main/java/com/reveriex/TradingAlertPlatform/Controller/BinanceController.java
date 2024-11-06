package com.reveriex.TradingAlertPlatform.Controller;

import com.reveriex.TradingAlertPlatform.Services.BinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class BinanceController {

    private final BinanceService binanceService;

    @Autowired
    public BinanceController(BinanceService binanceService) {
        this.binanceService = binanceService;
    }

    @GetMapping("/binance/balance")
    public String getBalances(Model model) {
        try {
            List<Map<String, String>> spotBalances = binanceService.getSpotBalance();
            List<Map<String, String>> futuresBalances = binanceService.getFuturesBalance();
            model.addAttribute("spotBalances", spotBalances);
            model.addAttribute("futuresBalances", futuresBalances);
        } catch (Exception e) {
            model.addAttribute("error", "Binance bakiyeleri alınamadı: " + e.getMessage());
        }
        return "balances";
    }
}
