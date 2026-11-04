package com.eric.phonebook.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eric.phonebook.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByOwnerUsernameOrderByNameAsc(String ownerUsername);
    Optional<Contact> findByIdAndOwnerUsername(Long id, String ownerUsername);
    Optional<Contact> findByNameIgnoreCase(String name);
    Optional<Contact> findByEmail(String email);
}
