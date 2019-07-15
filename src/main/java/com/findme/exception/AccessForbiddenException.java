package com.findme.exception;

public class AccessForbiddenException extends BadRequestException {

    public AccessForbiddenException(String message) {
        super(message);
    }
}
