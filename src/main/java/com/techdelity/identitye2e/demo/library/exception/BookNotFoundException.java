package com.techdelity.identitye2e.demo.library.exception;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException() {
    super("Book not found in library");
  }
}
