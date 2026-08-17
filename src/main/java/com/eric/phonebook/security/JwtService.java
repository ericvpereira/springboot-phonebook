package com.eric.phonebook.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey key;
	private final long expiration;

	public JwtService(@Value("${security.jwt.secret}") String secret,
			@Value("${security.jwt.expiration}") long expiration) {

		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expiration = expiration;
	}

	public String generateToken(String username) {

		Date now = new Date();

		Date expirationDate = new Date(now.getTime() + expiration);

		return Jwts.builder().subject(username).issuedAt(now).expiration(expirationDate).signWith(key).compact();

	}

	public String extractUsername(String token) {
		return extractClaims(token).getSubject();

	}

	public boolean isTokenValid(String token, String username) {

		try {

			String tokenUsername = extractUsername(token);

			return tokenUsername.equals(username) && !isTokenExpired(token);

		} catch (Exception e) {

			return false;
		}

	}

	private boolean isTokenExpired(String token) {

		return extractClaims(token).getExpiration().before(new Date());

	}

	private Claims extractClaims(String token) {

		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

}
