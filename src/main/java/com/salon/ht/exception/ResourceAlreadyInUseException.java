package com.salon.ht.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
public class ResourceAlreadyInUseException extends RuntimeException{
    private final String resourceName;
    private final String fieldName;
    private final transient  Object fieldValue;

    public ResourceAlreadyInUseException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s đã được sử dụng với %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
