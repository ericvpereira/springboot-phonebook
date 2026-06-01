package com.eric.phonebook.dto;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Contact data")
public class ContactDTO {

	@Schema(description = "Contact id", example = "1")
	private Long id;

	@NotBlank(message = "Name is required")
	@Schema(description = "Contact name", example = "Eric Vieira")
	private String name;

	@NotBlank(message = "Phone is required")
	@Schema(description = "Phone number", example = "11999999999")
	private String phone;

	@Email(message = "Invalid email")
	@Schema(description = "Email address", example = "eric@gmail.com")
	private String email;

	private ContactType type;

	@Valid
	private AddressDTO address;

	public ContactDTO() {
	}

	// ================= CONSTRUCTOR ENTITY -> DTO =================

	public ContactDTO(Contact entity) {
		id = entity.getId();
		name = entity.getName();
		phone = entity.getPhone();
		email = entity.getEmail();
		type = entity.getType();
		address = new AddressDTO(entity.getAddress());
	}

	// ================= DTO -> ENTITY =================

	public Contact toEntity() {

		Contact contact = new Contact();

		contact.setId(id);
		contact.setName(name);
		contact.setPhone(phone);
		contact.setEmail(email);
		contact.setType(type);
		contact.setAddress(address.toEntity());

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

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

}
