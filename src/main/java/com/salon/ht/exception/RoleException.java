package com.salon.ht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RoleException extends RuntimeException {
    public RoleException() {
    }

    public RoleException(String message) {
        super(message);
    }

    public RoleException(Throwable cause) {
        super(cause);
    }
}
