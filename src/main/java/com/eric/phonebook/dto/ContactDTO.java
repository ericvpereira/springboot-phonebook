package com.eric.phonebook.dto;

import com.eric.phonebook.entities.Address;
import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactDTO {

	private Long id;

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Phone is required")
	private String phone;

	@Email(message = "Invalid email")
	private String email;

	private ContactType type;

	private Address address;

	public ContactDTO() {
	}

	// ================= CONSTRUCTOR ENTITY -> DTO =================

	public ContactDTO(Contact entity) {
		id = entity.getId();
		name = entity.getName();
		phone = entity.getPhone();
		email = entity.getEmail();
		type = entity.getType();
		address = entity.getAddress();
	}

	// ================= DTO -> ENTITY =================

	public Contact toEntity() {

		Contact contact = new Contact();

		contact.setId(id);
		contact.setName(name);
		contact.setPhone(phone);
		contact.setEmail(email);
		contact.setType(type);
		contact.setAddress(address);

		return contact;

	}

	// ================= GETTERS / SETTERS =================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
