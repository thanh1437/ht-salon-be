package com.salon.ht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UploadFileException extends RuntimeException {
    private final String message;

    public UploadFileException(String message) {
        super(message);
        this.message = message;
    }
}
