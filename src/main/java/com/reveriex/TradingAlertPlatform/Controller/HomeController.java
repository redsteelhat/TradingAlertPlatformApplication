package com.reveriex.TradingAlertPlatform.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        // Ana sayfada kullanılacak model verilerini buraya ekleyebilirsiniz
        return "index";  // index.html dosyasına yönlendirir
    }
}
