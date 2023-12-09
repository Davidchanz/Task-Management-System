package com.effectivemobile.TaskManagementSystem.advice;

import com.effectivemobile.TaskManagementSystem.dto.response.ApiErrorDto;
import com.effectivemobile.TaskManagementSystem.dto.response.ErrorDto;
import com.effectivemobile.TaskManagementSystem.exception.*;
import com.effectivemobile.TaskManagementSystem.exception.auth.UserLoginException;
import com.effectivemobile.TaskManagementSystem.exception.exist.EmailAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.exist.UserAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.PriorityNotFoundException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.StatusNotFoundException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.TaskNotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorDto> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(objectError -> new ErrorDto(ex.getObjectName(), objectError.getDefaultMessage()))
                .collect(Collectors.toList());
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                errors.toArray(new ErrorDto[0])
        );
        return handleExceptionInternal(ex, errorDto, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {
            UserAlreadyExistException.class,
            EmailAlreadyExistException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.CONFLICT,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            TaskNotFoundException.class,
            PriorityNotFoundException.class,
            StatusNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNotFound(
            RuntimeException ex, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.NOT_FOUND,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {
            AccessToResourceDeniedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseEntity<Object> handleForbidden(
            RuntimeException ex, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.FORBIDDEN,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {
            InvalidTokenRequestException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<Object> handleInvalidToken(
            RuntimeException ex, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.UNAUTHORIZED,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = {
            UserLoginException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<Object> handleLoginBadCredentials(
            RuntimeException ex, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.UNAUTHORIZED,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), "Bad credentials! Invalid Username or Password")
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = {
            RequiredRequestParamIsMissingException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleRequiredRequestParamIsMissing(
            RuntimeException ex, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.NOT_FOUND,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiErrorDto errorDto = new ApiErrorDto(HttpStatus.METHOD_NOT_ALLOWED,
                ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
                new ErrorDto(ex.getClass().getName(), ex.getMessage())
        );
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request);
    }


}
