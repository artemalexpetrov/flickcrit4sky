package com.flickcrit.app.infrastructure.api.advice;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import jakarta.annotation.Nonnull;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BINDING_ERRORS_PROBLEM_PROPERTY = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @Nonnull MethodArgumentNotValidException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatusCode status,
        @Nonnull WebRequest request) {

        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach((fieldError) -> {
            String defaultMessage = fieldError.getDefaultMessage();
            if (Objects.nonNull(defaultMessage)) {
                errors.put(fieldError.getField(), defaultMessage);
            }
        });

        bindingResult.getGlobalErrors().forEach((globalError) -> {
            String defaultMessage = globalError.getDefaultMessage();
            if (Objects.nonNull(defaultMessage)) {
                errors.put(globalError.getObjectName(), defaultMessage);
            }
        });

        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setProperty(BINDING_ERRORS_PROBLEM_PROPERTY, errors);
        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<Object> handleEntityNotFoundException(Exception e, WebRequest request) {
        return doHandleException(e, request, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> doHandleException(Exception e, WebRequest request, HttpStatus status) {
        ProblemDetail detail = buildProblemDetail(request, e, status);
        return handleExceptionInternal(e, detail, HttpHeaders.EMPTY, status, request);
    }

    private ProblemDetail buildProblemDetail(WebRequest request, Exception e, HttpStatus status) {
        return ErrorResponse
            .builder(e, status, e.getMessage()).build()
            .updateAndGetBody(this.getMessageSource(), request.getLocale());
    }
}
