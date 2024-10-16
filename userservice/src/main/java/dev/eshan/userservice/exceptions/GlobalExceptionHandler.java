package dev.eshan.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public void handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void handleGlobalException(Exception ex, WebRequest request) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public void handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists: " + ex.getMessage());
    }
}