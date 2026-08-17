package com.eric.phonebook.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eric.phonebook.entities.User;
import com.eric.phonebook.enums.Role;
import com.eric.phonebook.repositories.UserRepository;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		return args -> {

			if (userRepository.findByUsername("admin").isEmpty()) {

				User user = new User("admin", passwordEncoder.encode("123456"), Role.ADMIN);

				userRepository.save(user);

				System.out.println("Usuário admin criado!");

			}

		};
	}

}
