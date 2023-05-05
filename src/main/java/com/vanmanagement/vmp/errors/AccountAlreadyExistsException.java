package com.vanmanagement.vmp.errors;

import org.springframework.dao.DataIntegrityViolationException;

public class AccountAlreadyExistsException extends DataIntegrityViolationException {

    public AccountAlreadyExistsException(String message) {
        super(message);
    }

    public AccountAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}