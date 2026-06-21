package com.eric.phonebook.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;
import com.eric.phonebook.exceptions.ContactNotFoundException;
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
	
	@Test
	void shouldFindContactById() {
		
		Contact contact = new Contact(
									"Eric", 
									"11999999999",
									"eric@email.com",
									ContactType.FRIEND
									);
		
		when(repository.findById(1L))
			.thenReturn(Optional.of(contact));
		
		Contact result = service.findById(1L);
		
		assertEquals("Eric", result.getName());
		
	}
	
	@Test
	void shouldThrowExceptionWhenContactNotFound() {
		
		when(repository.findById(99L))
			.thenReturn(Optional.empty());
		
		assertThrows(
				ContactNotFoundException.class,
				() -> service.findById(99L)
				);
		
	}
	
	@Test
	void shouldDeleteContact() {
		
		Contact contact = new Contact(
									"Eric",
									"11999999999",
									"eric@email.com",
									ContactType.FRIEND
								);
		
		when(repository.findById(1L))
		.thenReturn(Optional.of(contact));
		
		service.deleteContact(1L);
		
		verify(repository).delete(contact);
	}
	
	void shouldUpdateContact() {
		
		Contact existing = new Contact(
					"Eric",
					"11911111111",
					"old@email.com",
					ContactType.FRIEND
				);
		
		Contact updated = new Contact(
									"Eric Vieira",
									"11999999999",
									"new@email.com",
									ContactType.WORK
								);
		
		when(repository.findById(1L))
			.thenReturn(Optional.of(existing));
		
		when(repository.save(any(Contact.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));
		
		Contact result = service.updateContact(1L, updated);
		
		assertEquals("Eric Vieira", result.getName());
		assertEquals("new@email.com", result.getEmail());
		
	}

}
