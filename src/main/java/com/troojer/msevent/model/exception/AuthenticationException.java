package com.troojer.msevent.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NotBlank
public class AuthenticationException extends RuntimeException{
    private String message;
}
