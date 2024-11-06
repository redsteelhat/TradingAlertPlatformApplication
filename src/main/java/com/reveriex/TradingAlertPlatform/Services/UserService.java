package com.reveriex.TradingAlertPlatform.Services;

import com.reveriex.TradingAlertPlatform.Entity.User;
import com.reveriex.TradingAlertPlatform.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Kullanıcı kaydetme işlemi
    public User saveUser(User user, PasswordEncoder passwordEncoder) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword())); // Şifreyi encode et
        return userRepository.save(user);
    }

    // Kullanıcıyı kullanıcı adına göre bulma
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Kullanıcı adı kontrolü
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // E-posta kontrolü
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Profil fotoğrafı güncelleme
    public void updateUserProfilePicture(String profilePicturePath) {
        String username = getLoggedInUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfilePicture(profilePicturePath);
            userRepository.save(user);
        }
    }

    // Kullanıcı detaylarını yükleme
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    // Giriş yapan kullanıcının adını döndürme
    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
