package com.eric.phonebook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.eric.phonebook.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailsService userDetailsService;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {

		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				// Desabilita CSRF porque a API utiliza JWT
				.csrf(csrf -> csrf.disable())

				// API REST sem sessão
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Tratamento de autenticação e autorização
				.exceptionHandling(exception -> exception

						// 401 - usuário não autenticado
						.authenticationEntryPoint((request, response, authException) -> {

							System.out.println("================================");
							System.out.println(">>> AUTHENTICATION ERROR");
							System.out.println("URI: " + request.getRequestURI());
							System.out.println("ERRO: " + authException.getMessage());
							System.out.println("================================");

							response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
						})

						// 403 - usuário autenticado, mas sem permissão
						.accessDeniedHandler((request, response, accessDeniedException) -> {

							System.out.println("================================");
							System.out.println(">>> ACCESS DENIED HANDLER");
							System.out.println("URI: " + request.getRequestURI());

							System.out.println("USUARIO: " + SecurityContextHolder.getContext().getAuthentication());

							System.out.println("EXCEPTION: " + accessDeniedException.getClass().getName());

							System.out.println("MENSAGEM: " + accessDeniedException.getMessage());

							System.out.println("================================");

							response.sendError(HttpServletResponse.SC_FORBIDDEN);
						}))

				// Regras de acesso
				.authorizeHttpRequests(auth -> auth

						// Endpoints públicos
						.requestMatchers("/auth/login", "/auth/register", "/swagger-ui/**", "/swagger-ui.html",
								"/v3/api-docs/**", "/error")
						.permitAll()

						// Somente ADMIN
						.requestMatchers("/users/**").hasRole("ADMIN")

						// Qualquer usuário autenticado
						.requestMatchers("/contacts/**").authenticated()

						// Todo o restante exige autenticação
						.anyRequest().authenticated())

				// Provider responsável pela autenticação
				.authenticationProvider(authenticationProvider())

				// JWT antes do filtro padrão do Spring Security
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}
}