package com.eric.phonebook.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eric.phonebook.dto.auth.LoginRequest;
import com.eric.phonebook.dto.auth.LoginResponse;
import com.eric.phonebook.dto.auth.RegisterRequest;
import com.eric.phonebook.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService service;

	public AuthController(AuthService service) {
		this.service = service;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

		return ResponseEntity.ok(service.login(request));
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

		service.register(request);

		return ResponseEntity.ok().build();
	}
}