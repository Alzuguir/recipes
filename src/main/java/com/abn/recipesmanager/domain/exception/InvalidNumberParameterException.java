package com.abn.recipesmanager.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNumberParameterException extends RuntimeException {
    public InvalidNumberParameterException(String id) {
        super("Invalid parameter value: " + id + ", please input a number.");
    }
}