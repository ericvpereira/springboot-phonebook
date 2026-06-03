package com.eric.phonebook.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.exceptions.ContactNotFoundException;
import com.eric.phonebook.repositories.ContactRepository;

import jakarta.validation.ValidationException;

@Service
public class ContactService {

	private final ContactRepository repository;

	public ContactService(ContactRepository repository) {
		this.repository = repository;
	}

	// ================= SAVE =================

	public Contact addContact(Contact contact) {

		if (repository.findByEmail(contact.getEmail()).isPresent()) {
			throw new ValidationException("Email already registered");
		}

		return repository.save(contact);
	}

	// ================= FIND ALL =================

	public List<Contact> listAll() {
		return repository.findAll();
	}

	public Page<Contact> listAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	// ================= FIND BY ID =================

	public Contact findById(Long id) {

		Contact entity = repository.findById(id).orElseThrow(ContactNotFoundException::new);

		return entity;
	}

	// ================= DELETE =================

	public void deleteContact(Long id) {

		Contact contact = repository.findById(id).orElseThrow(ContactNotFoundException::new);

		repository.delete(contact);
	}

	// ================= UPDATE =================

	public Contact updateContact(Long id, Contact updateContact) {

		Contact existingContact = repository.findById(id).orElseThrow(ContactNotFoundException::new);

		existingContact.setName(updateContact.getName());
		existingContact.setPhone(updateContact.getPhone());
		existingContact.setEmail(updateContact.getEmail());
		existingContact.setType(updateContact.getType());
		existingContact.setAddress(updateContact.getAddress());

		return repository.save(existingContact);
	}

	// ================= FIND BY NAME =================

	public Contact findByName(String name) {

		return repository.findByNameIgnoreCase(name).orElseThrow(ContactNotFoundException::new);
	}

}
