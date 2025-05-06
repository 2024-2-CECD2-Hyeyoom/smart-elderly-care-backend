package com.example.smart_elderly_care.exception;

import org.springframework.http.HttpStatus;

public class CareServerException extends CareException {

    public CareServerException(final String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}