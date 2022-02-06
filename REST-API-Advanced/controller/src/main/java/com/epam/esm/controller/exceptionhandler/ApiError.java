package com.epam.esm.controller.exceptionhandler;

import lombok.Data;

@Data
public class ApiError {

    private final int code;
    private final String message;
}
