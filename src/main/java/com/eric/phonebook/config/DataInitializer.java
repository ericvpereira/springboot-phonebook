package com.eric.phonebook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.eric.phonebook.entities.User;
import com.eric.phonebook.enums.Role;
import com.eric.phonebook.repositories.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository users,
            PasswordEncoder encoder,
            @Value("${PHONEBOOK_ADMIN_USERNAME:}") String adminUsername,
            @Value("${PHONEBOOK_ADMIN_PASSWORD:}") String adminPassword) {
        return args -> {
            if (!StringUtils.hasText(adminUsername) && !StringUtils.hasText(adminPassword)) {
                return; // Do not provision predictable accounts by default.
            }
            if (!StringUtils.hasText(adminUsername) || !StringUtils.hasText(adminPassword)) {
                throw new IllegalStateException("Configure both PHONEBOOK_ADMIN_USERNAME and PHONEBOOK_ADMIN_PASSWORD");
            }
            if (adminPassword.length() < 12) {
                throw new IllegalStateException("PHONEBOOK_ADMIN_PASSWORD must have at least 12 characters");
            }
            if (users.findByUsername(adminUsername).isEmpty()) {
                users.save(new User(adminUsername, encoder.encode(adminPassword), Role.ADMIN));
            }
        };
    }
}
