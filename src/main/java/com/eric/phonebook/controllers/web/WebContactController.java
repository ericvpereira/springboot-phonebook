package com.eric.phonebook.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eric.phonebook.dto.ContactDTO;
import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.services.ContactService;

@Controller
@RequestMapping("/web/contacts")
public class WebContactController {

	private final ContactService service;

	public WebContactController(ContactService service) {
		this.service = service;
	}

	// ================= LIST CONTACTS =================

	@GetMapping
	public String findAll(Model model) {

		model.addAttribute("contacts", service.listAll());

		return "contacts";
	}

	// ================= OPEN CREATE FORM =================

	@GetMapping("/new")
	public String newContact(Model model) {

		model.addAttribute("contact", new ContactDTO());

		return "form";
	}

	// ================= SAVE CONTACT =================

	@PostMapping("/save")
	public String save(ContactDTO dto) {

		service.addContact(dto.toEntity());

		return "redirect:/web/contacts";
	}

	// ================= OPEN EDIT FORM =================

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		Contact dto = service.findById(id);

		model.addAttribute("contact", dto);

		return "form";
	}

	// ================= UPDATE CONTACT =================

	@PostMapping("/update/{id}")
	public String update(@PathVariable Long id, ContactDTO dto) {

		service.updateContact(id, dto.toEntity());

		return "redirect:/web/contacts";
	}

	// ================= DELETE CONTACT =================

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {

		service.deleteContact(id);

		return "redirect:/web/contacts";
	}

}
