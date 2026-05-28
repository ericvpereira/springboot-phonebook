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

import com.eric.phonebook.dto.ContactDTO;
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
	public List<ContactDTO> findAll() {
		return service.listAll().stream().map(ContactDTO::new).toList();
	}

	@GetMapping("/{id}")
	public Contact findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping
	public ContactDTO insert(@Valid @RequestBody ContactDTO dto) {

		Contact entity = dto.toEntity();

		entity = service.addContact(entity);

		return new ContactDTO(entity);
	}

	@PutMapping("/{id}")
	public ContactDTO update(@PathVariable Long id, @Valid @RequestBody ContactDTO dto) {

		Contact entity = service.updateContact(id, dto.toEntity());

		return new ContactDTO(entity);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.deleteContact(id);
	}

}
