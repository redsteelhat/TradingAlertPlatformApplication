package com.reveriex.TradingAlertPlatform.Controller;

import com.reveriex.TradingAlertPlatform.Entity.User;
import com.reveriex.TradingAlertPlatform.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Kayıt sayfası
    @GetMapping("/auth/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Kayıt işlemi
    @PostMapping("/auth/register")
    public String registerUser(User user, Model model) {
        if (userService.existsByUsername(user.getUsername()) || userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Bu kullanıcı adı veya e-posta zaten kullanımda.");
            return "register";
        }

        userService.saveUser(user, passwordEncoder); // PasswordEncoder'ı geçiriyoruz
        return "redirect:/auth/login";
    }

    // Giriş sayfası
    @GetMapping("/auth/login")
    public String showLoginForm() {
        return "login";
    }

    // Giriş işlemi
    @PostMapping("/auth/login")
    public String login(String username, String password, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/"; // Ana sayfaya yönlendirme
        } catch (Exception e) {
            model.addAttribute("error", "Kullanıcı adı veya şifre hatalı.");
            return "login"; // Hata durumunda tekrar login sayfasına döner
        }
    }
}
