package com.example.smart_elderly_care.exception;

import com.example.smart_elderly_care.exception.code.ErrorReasonDTO;
import com.example.smart_elderly_care.exception.code.ErrorStatus;
import lombok.Getter;

@Getter
public class CareClientException extends CareException {

    private final ErrorReasonDTO reason;

    public CareClientException(final ErrorStatus errorStatus) {
        super(errorStatus.getHttpStatus(), errorStatus.getMessage());
        this.reason = errorStatus.getReason();
    }
}
