package com.eric.phonebook.services;

import java.util.List;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.exceptions.ContactNotFoundException;
import com.eric.phonebook.repositories.ContactRepository;

public class ContactService {

	private ContactRepository repository;

	public ContactService() {
	}

	public ContactService(ContactRepository repository) {
		this.repository = repository;
	}

	public void addContact(Contact contact) {
		repository.save(contact);
	}

	public Contact findByName(String name) {
		for (Contact contact : repository.findAll()) {
			if (contact.getName().equalsIgnoreCase(name)) {
				return contact;
			}
		}
		throw new ContactNotFoundException();
	}

	public List<Contact> listAll() {
		return repository.findAll();
	}
}
