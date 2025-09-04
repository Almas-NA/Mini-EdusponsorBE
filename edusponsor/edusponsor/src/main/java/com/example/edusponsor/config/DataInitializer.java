package com.example.edusponsor.config;

import com.example.edusponsor.entity.User;
import com.example.edusponsor.enums.UserRole;
import com.example.edusponsor.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123")); // 🔐 Change this to a strong password
                admin.setRole(UserRole.ADMIN);
                userRepository.save(admin);
                System.out.println("✅ Admin user created: username='admin', password='admin123'");
            }
        };
    }
}
