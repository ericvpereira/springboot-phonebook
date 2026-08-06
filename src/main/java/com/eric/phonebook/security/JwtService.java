package com.eric.phonebook.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET = "MinhaChaveSecretaMuitoGrandeComMaisDe32Caracteres";
	
	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
	
	public String generateTokens(String username) {
		
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+86400000))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		
		Claims claims =
				Jwts.parser()
					.verifyWith((javax.crypto.SecretKey) key)
					.build()
					.parseSignedClaims(token)
					.getPayload();
		return claims.getSubject();
		
	}

}
