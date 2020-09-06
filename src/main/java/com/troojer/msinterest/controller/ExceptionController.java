package com.troojer.msinterest.controller;

import ch.qos.logback.classic.Logger;
import com.troojer.msinterest.model.ExceptionDto;
import com.troojer.msinterest.model.exception.ForbiddenException;
import com.troojer.msinterest.model.exception.NotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionController {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = (error instanceof FieldError) ? ((FieldError)error).getField() : Objects.requireNonNull(error.getArguments())[1].toString();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleRuntimeException(Exception exc) {
        logger.warn("unexpected exception; message: ", exc);
        return new ResponseEntity<>(new ExceptionDto("unexpected error"), HttpStatus.valueOf(500));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEventException(Exception exc) {
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionDto> handleForbiddenException(Exception exc) {
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.FORBIDDEN);
    }
}
