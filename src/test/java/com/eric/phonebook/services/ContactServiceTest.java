package com.eric.phonebook.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
class ContactServiceTest {

    @Mock
    private ContactRepository repository;

    @InjectMocks
    private ContactService service;

    private Contact contact;

    @BeforeEach
    void setUp() {

        contact = new Contact(
                "Eric",
                "11999999999",
                "eric@email.com",
                ContactType.FRIEND
        );

        contact.setId(1L);
    }

    @Test
    void shouldFindAllContacts() {

        when(repository.findAll())
                .thenReturn(Arrays.asList(contact));

        List<Contact> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Eric", result.get(0).getName());

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldFindContactById() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(contact));

        Contact result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Eric", result.getName());

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenContactDoesNotExist() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ContactNotFoundException.class,
                () -> service.findById(1L)
        );

        verify(repository).findById(1L);
    }

    @Test
    void shouldInsertContact() {

        when(repository.save(any(Contact.class)))
                .thenReturn(contact);

        Contact result = service.insert(contact);

        assertEquals("Eric", result.getName());

        verify(repository).save(contact);
    }

    @Test
    void shouldUpdateContact() {

        Contact updatedContact = new Contact(
                "Eric Pereira",
                "11888888888",
                "ericpereira@email.com",
                ContactType.WORK
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(contact));

        when(repository.save(any(Contact.class)))
                .thenReturn(contact);

        Contact result =
                service.update(1L, updatedContact);

        assertEquals(
                "Eric Pereira",
                result.getName()
        );

        assertEquals(
                "11888888888",
                result.getPhone()
        );

        assertEquals(
                "ericpereira@email.com",
                result.getEmail()
        );

        assertEquals(
                ContactType.WORK,
                result.getType()
        );

        verify(repository).findById(1L);
        verify(repository).save(contact);
    }

    @Test
    void shouldDeleteContact() {

        when(repository.existsById(1L))
                .thenReturn(true);

        doNothing()
                .when(repository)
                .deleteById(1L);

        service.delete(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingContact() {

        when(repository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ContactNotFoundException.class,
                () -> service.delete(1L)
        );

        verify(repository).existsById(1L);

        verify(repository, never())
                .deleteById(any());
    }
}