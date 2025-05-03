package com.neighboursnack.mailservice.exception;

import com.neighboursnack.common.dto.ErrorResponseDTO;
import com.neighboursnack.common.exception.SmtpException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class SmtpGlobalRestExceptionHandler {

    @ExceptionHandler(SmtpException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDTO handleSmtpException(SmtpException se, HttpServletRequest request) {
        return ErrorResponseDTO.of(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                se.getMessage(),
                request.getRequestURI(),
                request.getMethod()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDTO handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return ErrorResponseDTO.of(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponseDTO handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        return ErrorResponseDTO.of(
                CONFLICT.value(),
                CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleGenericException(Exception ex, HttpServletRequest request) {
        return ErrorResponseDTO.of(
                INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred",
                request.getRequestURI(),
                request.getMethod()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDTO handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        return ErrorResponseDTO.of(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod()
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorResponseDTO handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return ErrorResponseDTO.of(
                METHOD_NOT_ALLOWED.value(),
                METHOD_NOT_ALLOWED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDTO handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                DefaultMessageSourceResolvable::getDefaultMessage,
                                Collectors.toList()
                        )
                ));

        return ErrorResponseDTO.of(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                request.getMethod(),
                fieldErrors
        );
    }

}
