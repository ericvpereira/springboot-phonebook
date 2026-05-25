package com.eric.phonebook.exceptions.handlers;

import java.time.LocalDateTime;

public class StandardError {

	private LocalDateTime timestamp;
	private Integer status;
	private String error;

	public StandardError() {
	}

	public StandardError(LocalDateTime timestamp, Integer status, String error) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
