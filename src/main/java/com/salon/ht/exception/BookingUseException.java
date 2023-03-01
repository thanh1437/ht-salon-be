package com.salon.ht.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
public class BookingUseException extends RuntimeException {
    public BookingUseException(String message) {
        super(message);
    }
}
