package com.eric.phonebook.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;
import com.eric.phonebook.exceptions.ContactNotFoundException;
import com.eric.phonebook.repositories.ContactRepository;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {
    @Mock private ContactRepository repository;
    @InjectMocks private ContactService service;
    private Contact contact;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("eric", null, List.of()));
        contact = new Contact("Eric", "11999999999", "eric@email.com", ContactType.FRIEND);
        contact.setId(1L);
        contact.setOwnerUsername("eric");
    }

    @AfterEach
    void cleanup() { SecurityContextHolder.clearContext(); }

    @Test
    void shouldFindAllContacts() {
        when(repository.findAllByOwnerUsernameOrderByNameAsc("eric")).thenReturn(List.of(contact));
        List<Contact> result = service.findAll();
        assertEquals(1, result.size());
        verify(repository).findAllByOwnerUsernameOrderByNameAsc("eric");
    }

    @Test
    void shouldFindContactById() {
        when(repository.findByIdAndOwnerUsername(1L,"eric")).thenReturn(Optional.of(contact));
        assertEquals("Eric", service.findById(1L).getName());
    }

    @Test
    void shouldThrowExceptionWhenContactDoesNotExist() {
        when(repository.findByIdAndOwnerUsername(1L,"eric")).thenReturn(Optional.empty());
        assertThrows(ContactNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void shouldInsertContact() {
        when(repository.save(any(Contact.class))).thenAnswer(inv -> inv.getArgument(0));
        Contact result = service.insert(contact);
        assertNull(result.getId());
        assertEquals("eric", result.getOwnerUsername());
        verify(repository).save(contact);
    }

    @Test
    void shouldUpdateContact() {
        Contact updated = new Contact("Eric Pereira", "11888888888", "new@email.com", ContactType.WORK);
        when(repository.findByIdAndOwnerUsername(1L,"eric")).thenReturn(Optional.of(contact));
        when(repository.save(contact)).thenReturn(contact);
        Contact result = service.update(1L,updated);
        assertEquals("Eric Pereira",result.getName());
        assertEquals(ContactType.WORK,result.getType());
        assertEquals("eric",result.getOwnerUsername());
    }

    @Test
    void shouldDeleteContact() {
        when(repository.findByIdAndOwnerUsername(1L,"eric")).thenReturn(Optional.of(contact));
        service.delete(1L);
        verify(repository).delete(contact);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingContact() {
        when(repository.findByIdAndOwnerUsername(1L,"eric")).thenReturn(Optional.empty());
        assertThrows(ContactNotFoundException.class, () -> service.delete(1L));
        verify(repository,never()).delete(any(Contact.class));
    }

    @Test
    void shouldNotAccessOtherUsersContacts() {
        when(repository.findByIdAndOwnerUsername(1L,"eric")).thenReturn(Optional.empty());
        assertThrows(ContactNotFoundException.class, () -> service.update(1L,
                new Contact("Hacker","123","hack@example.com",ContactType.OTHER)));
        verify(repository,never()).save(any(Contact.class));
        verify(repository,never()).findById(1L);
    }
}
