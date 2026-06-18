package com.eric.phonebook.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;
import com.eric.phonebook.repositories.ContactRepository;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
	
	@Mock
	private ContactRepository repository;
	
	@InjectMocks
	private ContactService service;
	
	@Test
	void shouldSaveContact() {
		
		Contact contact = new Contact(
									"Eric",
									"11999999999",
									"eric@gmail.com",
									ContactType.FRIEND
									);	
		
		when(repository.save(any(Contact.class)))
				.thenReturn(contact);
		
		Contact result = service.addContact(contact);
		
		assertNotNull(result);
		assertEquals("Eric", result.getName());
		
		verify(repository).save(contact);
		
	}

}
