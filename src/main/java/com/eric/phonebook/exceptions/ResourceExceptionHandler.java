package com.eric.phonebook.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ContactNotFoundException.class)
	public ResponseEntity<StandardError> contactNotFound(ContactNotFoundException e) {

		StandardError error = new StandardError(Instant.now(), HttpStatus.NOT_FOUND.value(), "Resource not found",
				e.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
}