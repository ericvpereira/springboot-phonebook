package com.eric.phonebook.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.exceptions.ContactNotFoundException;
import com.eric.phonebook.exceptions.DatabaseException;
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

	// ================= FIND BY ID =================

	public Contact findById(Long id) {

		return repository.findById(id).orElseThrow(() -> new ContactNotFoundException());
	}

	// ================= DELETE =================

	public void deleteContact(Long id) {

		try {

			Contact contact = findById(id);

			repository.delete(contact);

		} catch (Exception e) {

			throw new DatabaseException("Error deleting contact");

		}

	}

	// ================= UPDATE =================

	public Contact updateContact(Long id, Contact updateContact) {

		Contact existingContact = findById(id);

		existingContact.setName(updateContact.getName());
		existingContact.setPhone(updateContact.getPhone());
		existingContact.setEmail(updateContact.getEmail());
		existingContact.setType(updateContact.getType());
		existingContact.setAddress(updateContact.getAddress());

		return repository.save(existingContact);
	}

	// ================= FIND BY NAME =================

	public Contact findByName(String name) {

		return repository.findByNameIgnoreCase(name).orElseThrow(() -> new ContactNotFoundException());
	}

}
