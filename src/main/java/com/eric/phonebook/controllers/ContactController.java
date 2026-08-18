package com.eric.phonebook.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contacts")
public class ContactController {

	private final ContactService service;

	public ContactController(ContactService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Contact>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contact> findById(@PathVariable Long id) {

		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	public ResponseEntity<Contact> insert(@Valid @RequestBody Contact contact) {

		Contact saved = service.insert(contact);

		URI uri = URI.create("/contacts/" + saved.getId());

		return ResponseEntity.created(uri).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Contact> update(@PathVariable Long id, @Valid @RequestBody Contact contact) {

		return ResponseEntity.ok(service.update(id, contact));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {

		service.delete(id);

		return ResponseEntity.noContent().build();
	}
}