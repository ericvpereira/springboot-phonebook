package com.eric.phonebook.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.eric.phonebook.dto.auth.LoginRequest;
import com.eric.phonebook.dto.auth.LoginResponse;
import com.eric.phonebook.security.JwtService;

public class AuthService {

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public LoginResponse login(LoginRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		String token = jwtService.generateToken(request.getUsername());

		return new LoginResponse(token);

	}

}
