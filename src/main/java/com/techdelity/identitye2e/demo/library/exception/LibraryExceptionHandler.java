package com.techdelity.identitye2e.demo.library.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LibraryExceptionHandler extends ResponseEntityExceptionHandler {

@ExceptionHandler(value = AlreadyExistsException.class)
protected ResponseEntity<Object> resourceAlreadyExists(RuntimeException ex, WebRequest request) {
    return handleExceptionInternal(ex, "Book already exists", new HttpHeaders(),
        HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(value = BookNotFoundException.class)
  protected ResponseEntity<Object> notFound(RuntimeException ex, WebRequest request) {
    return handleExceptionInternal(ex, "Cannot find book", new HttpHeaders(),
        HttpStatus.NOT_FOUND, request);
  }

}


