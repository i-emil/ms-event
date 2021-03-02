package com.troojer.msevent.controller;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.ExceptionDto;
import com.troojer.msevent.model.exception.*;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
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

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionDto> handleRuntimeException(ValidationException exc) {
        if (exc.getCause() instanceof ClientException) {
            return new ResponseEntity<>(new ExceptionDto("default.service.temporaryError"), HttpStatus.valueOf(503));
        }
        logger.warn("unexpected exception; message: ", exc);
        return new ResponseEntity<>(new ExceptionDto("unexpected error"), HttpStatus.valueOf(500));
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

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ExceptionDto> handleInvalidEntityException(Exception exc) {
        logger.warn("message: ", exc);
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
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

    @ExceptionHandler(NoContentExcepton.class)
    public ResponseEntity<ExceptionDto> handleNoContentException(Exception exc) {
        logger.warn("message: ", exc);
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionDto> handleConflictException(Exception exc) {
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ExceptionDto handleClientException(ClientException exc) {
        logger.warn("message: ", exc);
        return new ExceptionDto("default.service.temporaryError");
    }


    @ExceptionHandler(FileFormatException.class)
    public ResponseEntity<ExceptionDto> handleFileFormatException(Exception exc) {
        logger.warn("message: ", exc);
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
