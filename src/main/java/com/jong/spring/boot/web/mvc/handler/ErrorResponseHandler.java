package com.jong.spring.boot.web.mvc.handler;

import com.jong.spring.boot.web.mvc.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ErrorResponseHandler extends ResponseEntityExceptionHandler {

  // 1. @RequestBody + @Valid
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
  ) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ErrorResponse.builder()
            .path(request.getContextPath())
            .message("파라미터가 유효하지 않습니다.")
            .validErrors(ex.getAllErrors().stream()
                .map(objectError -> objectError instanceof FieldError fieldError
                    ? ErrorResponse.ValidError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()
                    : ErrorResponse.ValidError.builder()
                        .message(objectError.getDefaultMessage())
                        .build())
                .toList())
            .timestamp(LocalDateTime.now())
            .build());
  }

  // 2. @ModelAttribute + @Validated
  @ExceptionHandler(BindException.class)
  protected ResponseEntity<Object> handleBindException(BindException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ErrorResponse.builder()
            .path(request.getContextPath())
            .message("파라미터가 유효하지 않습니다.")
            .validErrors(ex.getAllErrors().stream()
                .map(objectError -> objectError instanceof FieldError fieldError
                    ? ErrorResponse.ValidError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()
                    : ErrorResponse.ValidError.builder()
                        .message(objectError.getDefaultMessage())
                        .build())
                .toList())
            .timestamp(LocalDateTime.now())
            .build());
  }

  // 3. @RequestParam, @PathVariable + @Validated
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ErrorResponse.builder()
            .path(request.getContextPath())
            .message("파라미터가 유효하지 않습니다.")
            .validErrors(ex.getConstraintViolations().stream()
                .map(constraintViolation -> ErrorResponse.ValidError.builder()
                    .field(StreamSupport.stream(constraintViolation.getPropertyPath().spliterator(), false)
                        .filter(propertyPath -> propertyPath.getKind() == ElementKind.PARAMETER
                            || propertyPath.getKind() == ElementKind.PROPERTY
                            || propertyPath.getKind() == ElementKind.CONTAINER_ELEMENT)
                        .map(propertyPath -> propertyPath.getName())
                        .collect(Collectors.joining(".")))
                    .message(constraintViolation.getMessage())
                    .build())
                .toList())
            .timestamp(LocalDateTime.now())
            .build());
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
  ) {
    log.warn(ex.getMessage(), ex);
    return ResponseEntity.status(statusCode)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ErrorResponse.builder()
            .path(request.getContextPath())
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnexpectedException(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ErrorResponse.builder()
            .path(request.getContextPath())
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
  }

}
