package com.eric.phonebook.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;
import com.eric.phonebook.security.CustomUserDetailsService;
import com.eric.phonebook.security.JwtService;
import com.eric.phonebook.services.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ContactControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ContactService service;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;

	private Contact contact;

	@BeforeEach
	void setUp() {

		contact = new Contact("Eric", "11999999999", "eric@email.com", ContactType.FRIEND);

		contact.setId(1L);
	}

	@Test
	void shouldFindAllContacts() throws Exception {

		when(service.findAll()).thenReturn(Arrays.asList(contact));

		mockMvc.perform(get("/contacts")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("Eric")).andExpect(jsonPath("$[0].phone").value("11999999999"))
				.andExpect(jsonPath("$[0].email").value("eric@email.com"));

		verify(service).findAll();
	}

	@Test
	void shouldFindContactById() throws Exception {

		when(service.findById(1L)).thenReturn(contact);

		mockMvc.perform(get("/contacts/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Eric"));

		verify(service).findById(1L);
	}

	@Test
	void shouldCreateContact() throws Exception {

		when(service.insert(any(Contact.class))).thenReturn(contact);

		mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contact))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Eric"));

		verify(service).insert(any(Contact.class));
	}

	@Test
	void shouldUpdateContact() throws Exception {

		Contact updatedContact = new Contact("Eric Pereira", "11888888888", "ericpereira@email.com", ContactType.WORK);

		updatedContact.setId(1L);

		when(service.update(eq(1L), any(Contact.class))).thenReturn(updatedContact);

		mockMvc.perform(put("/contacts/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedContact))).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Eric Pereira"))
				.andExpect(jsonPath("$.phone").value("11888888888"));

		verify(service).update(eq(1L), any(Contact.class));
	}

	@Test
	void shouldDeleteContact() throws Exception {

		doNothing().when(service).delete(1L);

		mockMvc.perform(delete("/contacts/1")).andExpect(status().isNoContent());

		verify(service).delete(1L);
	}
}