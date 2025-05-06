package com.example.smart_elderly_care.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CareException extends RuntimeException {

    public final HttpStatus status;

    public CareException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

}
