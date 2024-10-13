package com.techdelity.identitye2e.demo.library.service;

import com.techdelity.identitye2e.demo.library.exception.AlreadyExistsException;
import com.techdelity.identitye2e.demo.library.exception.BookNotFoundException;
import com.techdelity.identitye2e.demo.library.exception.NoBookCopiesRemainingToBorrowException;
import com.techdelity.identitye2e.demo.library.model.Book;
import com.techdelity.identitye2e.demo.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimpleLibraryService implements LibraryService {

  private final BookRepository bookRepository;

  @Autowired
  public SimpleLibraryService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public Book addBook(Book book) {
    if (findBookByISBN(book.getIsbn()).isPresent()) {
      throw new AlreadyExistsException("Book already exists");
    }
    return bookRepository.save(book);
  }

  @Override
  public void removeBook(String isbn) {
    if (findBookByISBN(isbn).isEmpty()) {
      throw new BookNotFoundException();
    }
    bookRepository.deleteById(isbn);

  }

  @Override
  public Optional<Book> findBookByISBN(String isbn) {
    return bookRepository.findById(isbn);
  }

  @Override
  public List<Book> findBookByAuthor(String author) {
    return bookRepository.findByAuthor(author);
  }


  @Override
  public Optional<Book> borrowBook(String isbn) {
    Optional<Book> book = findBookByISBN(isbn);
    book.ifPresent(b -> {
      if (b.getCopiesAvailable().decrementAndGet() < 0) {
        throw new NoBookCopiesRemainingToBorrowException();
      }
    });
    return book;
  }

  @Override
  public void returnBook(String isbn) {
    Optional<Book> book = findBookByISBN(isbn);
    if (book.isEmpty()) {
      throw new BookNotFoundException();
    }
    book.ifPresent(b -> b.getCopiesAvailable().getAndIncrement());
  }
}
