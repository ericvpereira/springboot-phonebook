package com.eric.phonebook.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.phonebook.services.ContactService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContactControllerSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContactService contactService;

	@Test
	void deveRetornar401QuandoNaoAutenticado() throws Exception {

		mockMvc.perform(get("/contacts")).andExpect(status().isUnauthorized());

	}

	@Test
	@WithMockUser(username = "teste", roles = "USER")
	void devePermitirGetContactsParaUsuarioAutenticado() throws Exception {

		when(contactService.findAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/contacts")).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(username = "teste", roles = "USER")
	void deveRetornar403QuandoUsuarioComumTentaExcluirContato() throws Exception {

		mockMvc.perform(delete("/contacts/1")).andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void devePermitirExclusaoParaAdministrador() throws Exception {

		doNothing().when(contactService).delete(1L);

		mockMvc.perform(delete("/contacts/1")).andExpect(status().isNoContent());

	}

}
