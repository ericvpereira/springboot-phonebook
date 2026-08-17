package com.eric.phonebook.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eric.phonebook.dto.auth.LoginRequest;
import com.eric.phonebook.dto.auth.LoginResponse;
import com.eric.phonebook.dto.auth.RegisterRequest;
import com.eric.phonebook.entities.User;
import com.eric.phonebook.enums.Role;
import com.eric.phonebook.repositories.UserRepository;
import com.eric.phonebook.security.JwtService;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder passwordEncoder, JwtService jwtService) {

		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public LoginResponse login(LoginRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		String token = jwtService.generateToken(request.getUsername());

		return new LoginResponse(token);
	}

	public void register(RegisterRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {

			throw new RuntimeException("Usuário já existe");
		}

		User user = new User();

		user.setUsername(request.getUsername());

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole(Role.USER);

		userRepository.save(user);
	}
}