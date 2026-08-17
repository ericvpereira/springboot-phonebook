package com.eric.phonebook.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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

	@Transactional(readOnly = true)
	public List<Contact> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Contact findById(Long id) {

		return repository.findById(id).orElseThrow(() -> new ContactNotFoundException("Contato não encontrado: " + id));
	}

	@Transactional
	public Contact insert(Contact contact) {
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

		if (!repository.existsById(id)) {
			throw new ContactNotFoundException("Contato não encontrado: " + id);
		}

		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw e;
		}
	}
}