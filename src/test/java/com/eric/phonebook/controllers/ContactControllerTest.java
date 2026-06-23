package com.eric.phonebook.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;
import com.eric.phonebook.services.ContactService;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContactService service;

	@Test
	void shouldReturnContactById() throws Exception {

		Contact contact = new Contact("Eric", "11999999999", "eric@gmail.com", ContactType.FRIEND);

		contact.setId(1L);

		when(service.findById(1L)).thenReturn(contact);

		mockMvc.perform(get("/contacts/1")).andExpect(status().isOk());

	}

	@Test
	void shouldReturnPageOfContacts() throws Exception {

		mockMvc.perform(get("/contacts")).andExpect(status().isOk());
	}

	@Test
	void shouldCreateContact() throws Exception {

		String json = """
				{
					"name":"Eric",
					"phone":"11999999999",
					"email":"eric@gmail.com",
					"type":"FRIEND"
				}
				""";

		Contact contact = new Contact("Eric", "11999999999", "eric@gmail.com", ContactType.FRIEND);

		when(service.addContact(org.mockito.ArgumentMatchers.any())).thenReturn(contact);

		mockMvc.perform(post("/contacts").contentType(APPLICATION_JSON).content(json)).andExpect(status().isCreated());

	}

	@Test
	void shouldDeleteContact() throws Exception {

		mockMvc.perform(delete("/contacts/1")).andExpect(status().isNoContent());

	}

	@Test
	void shouldUpdateContact() throws Exception {

		String json = """
				{
					"name":"Eric Updated",
					"phone":"11988888888",
					"email":"novo@gmail.com",
					"type":"WORK"
				}
				""";

		Contact updated = new Contact("Eric Updated", "11988888888", "novo@gmail.com", ContactType.WORK);

		when(service.updateContact(org.mockito.ArgumentMatchers.eq(1L),
				org.mockito.ArgumentMatchers.any(Contact.class))).thenReturn(updated);

		mockMvc.perform(put("/contacts/1").contentType(APPLICATION_JSON).content(json)).andExpect(status().isOk());

	}
	
	@Test
	void shouldFindContactByName() throws Exception {
		
		Contact contact = new Contact(
					"Eric",
					"11999999999",
					"eric@gmail.com",
					ContactType.FRIEND
				);
		
		when(service.findByName("Eric"))
			.thenReturn(contact);
		
		mockMvc.perform(
				get("/contacts/search")
					.param("name", "Eric"))
				.andExpect(status().isOk());
		
	}

}
