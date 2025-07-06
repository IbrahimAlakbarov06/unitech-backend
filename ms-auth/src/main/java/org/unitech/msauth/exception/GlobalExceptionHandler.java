package org.unitech.msauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.unitech.msauth.model.dto.resposne.ErrorResponse;
import org.unitech.msauth.model.enums.ErrorMessage;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder()
                .uuid(UUID.randomUUID())
                .message(ex.getMessage())
                .error(ErrorMessage.INVALID_CREDENTIALS.getDisplayName())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .uuid(UUID.randomUUID())
                .message(ex.getMessage())
                .error(ErrorMessage.NOT_FOUND.getDisplayName())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .uuid(UUID.randomUUID())
                .message(ex.getMessage())
                .error(ErrorMessage.ALREADY_EXISTS.getDisplayName())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .error(ErrorMessage.METHOD_ARGUMENT_NOT_VALID.getDisplayName())
                .message("MethodArgumentNotValidException happened")
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
                .error(ErrorMessage.INTERNAL_SERVER_ERROR.getDisplayName())
                .message(exception.getMessage())
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
