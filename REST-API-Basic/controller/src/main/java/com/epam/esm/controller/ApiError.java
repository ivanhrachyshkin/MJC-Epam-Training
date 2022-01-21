package com.epam.esm.controller;

public class ApiError {

    private final int code;
    private final String message;

    public ApiError(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
