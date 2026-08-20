package com.eric.phonebook.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void deveRetornar401QuandoNaoAutenticado() throws Exception {

		mockMvc.perform(get("/users")).andExpect(status().isUnauthorized());
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