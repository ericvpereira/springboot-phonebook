package com.eric.phonebook.config;

import java.util.regex.Pattern;

public class Validator {

	public static final String REGEX_PHONE_BR = "^\\(?[1-9]{2}\\)? ?(?:[2-8]|9[1-9])[0-9]{3}\\-?[0-9]{4}$";
	public static final String REGEX_EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
	public static final Pattern PATTERN = Pattern.compile(REGEX_EMAIL);

	public static boolean validatePhone(String phone) {
		if (phone == null)
			return false;
		return phone.matches(REGEX_PHONE_BR);
	}

	public static boolean validateEmail(String email) {
		if (email == null || email.isBlank()) {
			return false;
		}
		return PATTERN.matcher(email).matches();

	}

}
