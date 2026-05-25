package com.eric.phonebook.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.phonebook.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

	Optional<Contact> findByNameIgnoreCase(String name);

	Optional<Contact> findByEmail(String email);

}
