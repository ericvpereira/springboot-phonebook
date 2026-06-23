package com.eric.phonebook.dto;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContactInsertDTO {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Phone is required")
	private String phone;

	@Email
	private String email;

	@NotNull
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
		if (address != null) {
			contact.setAddress(address.toEntity());
		}

		return contact;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

}
