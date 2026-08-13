package com.eric.phonebook.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private JwtService jwtService;

    private final String secret =
            "minha-chave-secreta-super-segura-com-mais-de-32-caracteres";

    private final long expiration = 3600000;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService(
                secret,
                expiration
        );
    }

    @Test
    void shouldGenerateToken() {

        String token = jwtService.generateToken("Eric");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameFromToken() {

        String token = jwtService.generateToken("Eric");

        String username =
                jwtService.extractUsername(token);

        assertEquals("Eric", username);
    }

    @Test
    void shouldValidateValidToken() {

        String token =
                jwtService.generateToken("Eric");

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        "Eric"
                );

        assertTrue(valid);
    }

    @Test
    void shouldRejectTokenWithDifferentUsername() {

        String token =
                jwtService.generateToken("Eric");

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        "Joao"
                );

        assertFalse(valid);
    }
}