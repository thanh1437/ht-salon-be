package com.salon.ht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class DeptRegistrationException extends RuntimeException {
    private String deptName;
    private String message;

    public DeptRegistrationException(String deptName, String message) {
        super(String.format("Fail to register department [%s] : '%s'", deptName, message));
        this.deptName = deptName;
        this.message = message;
    }
}
