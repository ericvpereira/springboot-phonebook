package com.eric.phonebook.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eric.phonebook.dto.auth.LoginRequest;
import com.eric.phonebook.dto.auth.LoginResponse;
import com.eric.phonebook.repositories.UserRepository;
import com.eric.phonebook.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@InjectMocks
	private AuthService authService;

	@Test
	void deveRealizarLoginComSucesso() {

		LoginRequest request = new LoginRequest();

		request.setUsername("admin");
		request.setPassword("123456");

		when(jwtService.generateToken("admin")).thenReturn("token-falso");

		LoginResponse response = authService.login(request);

		assertNotNull(response);

	}

	@Test
	void deveFalharQuandoSenhaEstiverIncorreta() {

		LoginRequest request = new LoginRequest();

		request.setUsername("admin");
		request.setPassword("senha-errada");

		when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Credenciais inválidas"));

		assertThrows(BadCredentialsException.class, () -> authService.login(request));

	}

}
