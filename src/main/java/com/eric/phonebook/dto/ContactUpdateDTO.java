package com.eric.phonebook.dto;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactUpdateDTO {

	@NotBlank
	private String name;

	private String phone;

	@Email
	private String email;

	private ContactType type;

	@Valid
	private AddressDTO address;

	// ================= DTO -> ENTITY =================

	public Contact toEntity() {

		Contact contact = new Contact();

		contact.setName(name);
		contact.setPhone(phone);
		contact.setEmail(email);
		contact.setType(type);
		contact.setAddress(address.toEntity());

		return contact;

	}

}
