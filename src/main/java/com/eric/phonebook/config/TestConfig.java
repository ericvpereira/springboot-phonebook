package com.eric.phonebook.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eric.phonebook.entities.Address;
import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.enums.ContactType;
import com.eric.phonebook.repositories.ContactRepository;
import com.eric.phonebook.services.ContactService;

@Configuration
public class TestConfig {

	private final ContactRepository contactRepository;

	TestConfig(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	@Bean
	CommandLineRunner run(ContactService service) {

		return args -> {

			// ================= ADDRESS =================

			Address address1 = new Address("Rua A", "São Paulo", "SP", "01234-000");

			Address address2 = new Address("Rua B", "Campinas", "SP", "04567-000");

			// ================= CONTACTS =================

			Contact contact1 = new Contact("Eric", "11999999999", "eric@email.com", ContactType.FRIEND);

			contact1.setAddress(address1);

			Contact contact2 = new Contact("Maria", "11888888888", "maria@email.com", ContactType.WORK);

			contact2.setAddress(address2);

			// ================= SAVE =================

			service.addContact(contact1);
			service.addContact(contact2);

			// ================= SHOW =================

			System.out.println("\n===== CONTACTS SAVED =====");

			service.listAll().forEach(System.out::println);

		};
	}

}
