package com.eric.phonebook.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.phonebook.dto.auth.LoginRequest;
import com.eric.phonebook.dto.auth.LoginResponse;
import com.eric.phonebook.services.AuthService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
			@Valid @RequestBody LoginRequest request) {
		
		return ResponseEntity.ok(authService.login(request)
		);
	}

}
