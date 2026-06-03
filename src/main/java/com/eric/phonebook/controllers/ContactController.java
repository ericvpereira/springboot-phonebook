package com.eric.phonebook.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eric.phonebook.dto.ContactDTO;
import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.services.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contacts")
@Tag(name = "Contacts", description = "Phonebook management")
public class ContactController {

	private final ContactService service;

	public ContactController(ContactService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "List all contacts")
	public Page<ContactDTO> findAll(Pageable pageable) {

		return service.listAll(pageable).map(ContactDTO::new);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Find contact by id")
	public ContactDTO findById(@PathVariable Long id) {

		Contact contact = service.findById(id);

		return new ContactDTO(contact);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Created new contact")
	public ContactDTO insert(@Valid @RequestBody ContactDTO dto) {

		Contact entity = service.addContact(dto.toEntity());

		return new ContactDTO(entity);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update contact")
	public ContactDTO update(@PathVariable Long id, @Valid @RequestBody ContactDTO dto) {

		Contact entity = service.updateContact(id, dto.toEntity());

		return new ContactDTO(entity);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		service.deleteContact(id);
	}

	@GetMapping("/search")
	@Operation(summary = "Find contact by name")
	public ContactDTO findByName(@RequestParam String name) {

		return new ContactDTO(service.findByName(name));
	}

}
