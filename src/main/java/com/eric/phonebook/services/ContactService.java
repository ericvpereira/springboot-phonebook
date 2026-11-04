package com.eric.phonebook.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.exceptions.ContactNotFoundException;
import com.eric.phonebook.repositories.ContactRepository;

@Service
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new org.springframework.security.access.AccessDeniedException("Authentication required");
        }
        return authentication.getName();
    }

    @Transactional(readOnly = true)
    public List<Contact> findAll() {
        return repository.findAllByOwnerUsernameOrderByNameAsc(currentUsername());
    }

    @Transactional(readOnly = true)
    public Contact findById(Long id) {
        return repository.findByIdAndOwnerUsername(id, currentUsername())
                .orElseThrow(() -> new ContactNotFoundException("Contato não encontrado: " + id));
    }

    @Transactional
    public Contact insert(Contact contact) {
        // Ignore owner and id supplied by the client.
        contact.setId(null);
        contact.setOwnerUsername(currentUsername());
        return repository.save(contact);
    }

    @Transactional
    public Contact update(Long id, Contact contact) {
        Contact entity = findById(id);
        entity.setName(contact.getName());
        entity.setPhone(contact.getPhone());
        entity.setEmail(contact.getEmail());
        entity.setType(contact.getType());
        entity.setAddress(contact.getAddress());
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        Contact entity = findById(id);
        repository.delete(entity);
    }
}
