package com.eric.phonebook.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.eric.phonebook.entities.User;
import com.eric.phonebook.enums.Role;
import com.eric.phonebook.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	private CustomUserDetailsService service;
	
	@BeforeEach
	void setUp() {
		service = new CustomUserDetailsService(userRepository);
	}
	
	@Test
	void shouldLoadUserByUsername() {
		
		User user = new User(
				"Eric",
				"senha123",
				Role.USER
		);
		
		when(userRepository.findByUsername("Eric"))
			.thenReturn(Optional.of(user));
		
		UserDetails result = 
				service.loadUserByUsername("Eric");
		
		assertEquals("Eric", result.getUsername());
		assertEquals("senha123", result.getPassword());
		
		assertTrue(
			result.getAuthorities()
				.stream()
				.anyMatch(authority -> 
						authority.getAuthority()
							.equals("ROLE_USER"))
		);
		
		verify(userRepository)
			.findByUsername("Eric");
		
	}
	
	@Test
	void shouldThrowExceptionWhenUserNotFound() {
		
		when(userRepository.findByUsername("Eric"))
			.thenReturn(Optional.empty());
		
		assertThrows(
			UsernameNotFoundException.class,
			() -> service.loadUserByUsername("Eric")
		);
		
		verify(userRepository)
			.findByUsername("Eric");
		
	}
}
