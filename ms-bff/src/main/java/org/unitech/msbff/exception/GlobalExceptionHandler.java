package org.unitech.msbff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.unitech.msbff.dto.ErrorResponse;

import java.time.LocalDateTime;
import java.util.UUID;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(ServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                ErrorResponse.builder()
                        .uuid(UUID.randomUUID())
                        .message(ex.getMessage())
                        .error("SERVICE_UNAVAILABLE")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .uuid(UUID.randomUUID())
                        .message(ex.getMessage())
                        .error("NOT_FOUND")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .error("METHOD_ARGUMENT_NOT_VALID")
                .message("MethodArgumentNotValidException happened")
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .uuid(UUID.randomUUID())
                        .message("An unexpected error occurred")
                        .error("INTERNAL_SERVER_ERROR")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}