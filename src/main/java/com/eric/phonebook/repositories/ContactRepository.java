package com.eric.phonebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.phonebook.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
