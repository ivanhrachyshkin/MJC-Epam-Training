package com.epam.esm.view.exceptionhandler;

import lombok.Data;

@Data
public class ApiError {

    private final String code;
    private final String message;
}
