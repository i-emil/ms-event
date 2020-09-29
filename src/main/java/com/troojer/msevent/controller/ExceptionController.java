package com.troojer.msevent.controller;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.ExceptionDto;
import com.troojer.msevent.model.exception.AuthenticationException;
import com.troojer.msevent.model.exception.ClientException;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
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
        logger.warn("message: ", ex);
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
        logger.warn("message: ", exc);
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ExceptionDto> handleEventException(PropertyReferenceException exc) {
        logger.warn("message: ", exc);
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionDto> handleForbiddenException(Exception exc) {
        logger.warn("message: ", exc);
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDto> handleAuthenticationException(Exception exc) {
        logger.warn("message: ", exc);
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleClientException(ClientException exc) {
        logger.warn("message: ", exc);
        return new ExceptionDto("default.service.temporaryError");
    }
}
