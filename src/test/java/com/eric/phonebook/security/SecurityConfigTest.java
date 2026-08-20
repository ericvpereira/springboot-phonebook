package com.eric.phonebook.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.phonebook.config.SecurityConfig;

@WebMvcTest
@Import(SecurityConfig.class)
public class SecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;

	@Test
	void deveRetornar401QuandoNaoAutenticado() throws Exception {

		mockMvc.perform(get("/contacts")).andExpect(status().isUnauthorized());

	}

	@Test
	void deveRetornar403QuandoUsuarioNaoEhAdmin() throws Exception {

		mockMvc.perform(get("/users").with(user("eric").roles("USER"))).andExpect(status().isForbidden());

	}

	@Test
	void devePermitirAcessoQuandoUsuarioEhAdmin() throws Exception {

		mockMvc.perform(get("/users").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());

	}

}
