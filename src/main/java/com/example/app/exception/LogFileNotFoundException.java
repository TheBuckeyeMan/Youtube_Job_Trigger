package com.example.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;

public class LogFileNotFoundException extends HttpClientErrorException {

    public LogFileNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
