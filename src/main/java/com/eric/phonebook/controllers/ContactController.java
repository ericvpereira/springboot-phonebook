package com.eric.phonebook.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.services.ContactService;

@RestController
@RequestMapping("/contacts")
public class ContactController {

	private final ContactService service;

	public ContactController(ContactService service) {
		this.service = service;
	}

	@GetMapping
	public List<Contact> findAll() {
		return service.listAll();
	}

	@GetMapping("/{id}")
	public Contact findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping
	public Contact insert(@RequestBody Contact contact) {
		return service.addContact(contact);
	}

	@PutMapping("/{id}")
	public Contact update(@PathVariable Long id, @RequestBody Contact contact) {

		return service.updateContact(id, contact);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.deleteContact(id);
	}

}
