package com.eric.phonebook.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.phonebook.dto.auth.LoginResponse;
import com.eric.phonebook.security.CustomUserDetailsService;
import com.eric.phonebook.security.JwtService;
import com.eric.phonebook.services.AuthService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthService authService;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private CustomUserDetailsService userDetailsService;

	@Test
	void deveRealizarLoginComSucesso() throws Exception {

		when(authService.login(any())).thenReturn(new LoginResponse("token-falso"));

		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
				{
				    "username": "admin",
				    "password": "123456"
				}
				""")).andExpect(status().isOk());
	}

	@Test
	void deveRealizarRegistroComSucesso() throws Exception {

		doNothing().when(authService).register(any());

		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
				{
				    "username": "novoUsuario",
				    "password": "123456"
				}
				""")).andExpect(status().isOk());
	}
}