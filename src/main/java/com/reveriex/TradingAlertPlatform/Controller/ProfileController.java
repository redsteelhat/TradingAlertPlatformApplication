package com.reveriex.TradingAlertPlatform.Controller;

import com.reveriex.TradingAlertPlatform.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        String username = userService.getLoggedInUsername(); // Kullanıcının adı alınır
        model.addAttribute("username", username);
        model.addAttribute("profilePicture", "/images/default-profile.png"); // Varsayılan profil fotoğrafı
        return "profile";
    }
}
