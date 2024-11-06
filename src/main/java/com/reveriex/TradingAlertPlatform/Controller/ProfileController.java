package com.reveriex.TradingAlertPlatform.Controller;

import com.reveriex.TradingAlertPlatform.Entity.User;
import com.reveriex.TradingAlertPlatform.Services.BinanceService;
import com.reveriex.TradingAlertPlatform.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    private final UserService userService;
    private final BinanceService binanceService;

    @Autowired
    public ProfileController(UserService userService, BinanceService binanceService) {
        this.userService = userService;
        this.binanceService = binanceService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        String username = userService.getLoggedInUsername();
        User user = userService.findByUsername(username);

        if (user != null) {
            model.addAttribute("user", user); // Kullanıcı bilgilerini doğrudan aktar
            model.addAttribute("lastLogin", "03 Kasım 2024"); // Örnek son giriş zamanı
            model.addAttribute("totalTrades", 120); // Örnek işlem sayısı

            try {
                List<Map<String, String>> spotBalances = binanceService.getSpotBalance();
                List<Map<String, String>> futuresBalances = binanceService.getFuturesBalance();
                model.addAttribute("spotBalances", spotBalances);
                model.addAttribute("futuresBalances", futuresBalances);
                model.addAttribute("binanceConnected", true);
            } catch (Exception e) {
                model.addAttribute("binanceConnected", false);
            }
        } else {
            model.addAttribute("error", "Kullanıcı bulunamadı");
        }

        return "profile";
    }

    @PostMapping("/profile/upload")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        String uploadDir = System.getProperty("user.dir") + "/uploads/images/";

        try {
            // Dosya dizinini oluştur
            Files.createDirectories(Paths.get(uploadDir));

            // Dosya yolunu belirle
            String filePath = uploadDir + file.getOriginalFilename();

            // Dosyayı kaydet
            file.transferTo(new File(filePath));

            // Profil fotoğrafı yolunu güncelle
            userService.updateUserProfilePicture("/uploads/images/" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profile";
    }
}
