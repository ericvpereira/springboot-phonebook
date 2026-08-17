package com.eric.phonebook.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
	private JwtService jwtService;

	@Mock
	private CustomUserDetailsService userDetailsService;

	private JwtAuthenticationFilter filter;

	@BeforeEach
	void setUp() {
		filter = new JwtAuthenticationFilter(jwtService, userDetailsService);

		SecurityContextHolder.clearContext();
	}

	@Test
	void shouldAuthenticateUserWithValidToken() throws Exception {

		String token = "token-valido";
		String username = "Eric";

		UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(username)
				.password("123456").roles("USER").build();

		when(jwtService.extractUsername(token)).thenReturn(username);

		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

		when(jwtService.isTokenValid(token, username)).thenReturn(true);

		MockHttpServletRequest request = new MockHttpServletRequest();

		request.addHeader("Authorization", "Bearer " + token);

		MockHttpServletResponse response = new MockHttpServletResponse();

		MockFilterChain filterChain = new MockFilterChain();

		filter.doFilter(request, response, filterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		assertTrue(authentication.isAuthenticated());

		assertEquals(username, authentication.getName());

		verify(jwtService).extractUsername(token);

		verify(userDetailsService).loadUserByUsername(username);

		verify(jwtService).isTokenValid(token, username);
	}

	@Test
	void shouldContinueWithoutToken() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();

		MockHttpServletResponse response = new MockHttpServletResponse();

		MockFilterChain filterChain = new MockFilterChain();

		filter.doFilter(request, response, filterChain);

		assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
	}

	@Test
	void shouldContinueWithInvalidToken() throws Exception {

		String token = "token-invalido";

		when(jwtService.extractUsername(token)).thenThrow(new RuntimeException());

		MockHttpServletRequest request = new MockHttpServletRequest();

		request.addHeader("Authorization", "Bearer " + token);

		MockHttpServletResponse response = new MockHttpServletResponse();

		MockFilterChain filterChain = new MockFilterChain();

		filter.doFilter(request, response, filterChain);

		assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);

		verify(jwtService).extractUsername(token);
	}

	@Test
	void shouldNotAuthenticateWithInvalidToken() throws Exception {

		String token = "token-expirado";
		String username = "Eric";

		UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(username)
				.password("123456").roles("USER").build();

		when(jwtService.extractUsername(token)).thenReturn(username);

		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

		when(jwtService.isTokenValid(token, username)).thenReturn(false);

		MockHttpServletRequest request = new MockHttpServletRequest();

		request.addHeader("Authorization", "Bearer " + token);

		MockHttpServletResponse response = new MockHttpServletResponse();

		MockFilterChain filterChain = new MockFilterChain();

		filter.doFilter(request, response, filterChain);

		assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);

		verify(jwtService).isTokenValid(token, username);
	}
}