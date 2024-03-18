package com.playground.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.playground.model.ServerError;
import com.playground.model.ValidationError;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        ServerError serverError = new ServerError(ex.getMessage());

        return new ResponseEntity<>(serverError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { MissingPathVariableException.class })
    public ResponseEntity<Object> handleMissingPathVariableException(MissingPathVariableException ex,
            WebRequest request) {
        ServerError serverError = new ServerError("Required path variable is missing");

        return new ResponseEntity<>(serverError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}