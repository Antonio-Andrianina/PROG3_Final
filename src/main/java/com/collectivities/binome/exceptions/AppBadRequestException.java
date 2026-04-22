package com.collectivities.binome.exceptions;

public class AppBadRequestException extends RuntimeException {

    public AppBadRequestException(String message) {
        super(message);
    }

    public AppBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
