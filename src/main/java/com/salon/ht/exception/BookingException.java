package com.salon.ht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class BookingException extends RuntimeException {
    private String startTime;
    private String endTime;
    private String title;
    private String message;

    public BookingException(String message, String startTime, String endTime, String title) {
        super(String.format("Fail to booking with [%s] from [%s] to [%s]: '%s'",
                title, startTime, endTime, message));
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }


}
