package com.salon.ht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ImportUserException extends RuntimeException {

    public ImportUserException(String message) {
        super(message);
    }

    public ImportUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
