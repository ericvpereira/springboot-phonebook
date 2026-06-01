package com.eric.phonebook.dto;

import java.util.Objects;

import com.eric.phonebook.entities.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddressDTO {

	@NotBlank(message = "Street is required")
	@Size(max = 100, message = "Street cannot exceed 100 characters")
	private String street;

	@NotBlank(message = "City is required")
	@Size(max = 100, message = "City cannot exceed 100 characters")
	private String city;

	@NotBlank(message = "State is required")
	@Size(max = 2, message = "State need to have 2 characters")
	private String state;

	@NotBlank(message = "Zip code is required")
	@Size(max = 9)
	@Pattern(regexp = "\\d{5}-\\d{3}", message = "Zip code must be in format 00000-000")
	private String zipCode;

	public AddressDTO() {
	}

	public AddressDTO(Address entity) {
		street = entity.getStreet();
		city = entity.getCity();
		state = entity.getState();
		zipCode = entity.getZipCode();
	}

	public Address toEntity() {

		Address address = new Address();

		address.setStreet(street);
		address.setCity(city);
		address.setState(state);
		address.setZipCode(zipCode);

		return address;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, state, street, zipCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressDTO other = (AddressDTO) obj;
		return Objects.equals(city, other.city) && Objects.equals(state, other.state)
				&& Objects.equals(street, other.street) && Objects.equals(zipCode, other.zipCode);
	}
}
