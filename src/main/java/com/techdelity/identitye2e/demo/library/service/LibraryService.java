package com.techdelity.identitye2e.demo.library.service;

import com.techdelity.identitye2e.demo.library.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * The library service interface
 */
public interface LibraryService {

  /**
   * Add a new book to the library
   *
   * @param book the book to add
   * @return The book that has been added
   */
  Book addBook(Book book);

  /**
   * Remove a book from the library by the given isbn
   *
   * @param isbn the unique isbn identifier for the book
   */
  void removeBook(String isbn);

  /**
   * Find a book by the given isbn
   *
   * @param isbn the search isbn to find the book by
   * @return the search result book
   */
  Optional<Book> findBookByISBN(String isbn);

  /**
   * Find a books by a specific author
   *
   * @param author the author to search for books by
   * @return a list of books by the given auther
   */
  List<Book> findBookByAuthor(String author);

  /**
   * Borrow a book from the library
   *
   * @param isbn the identifier of the book to borrow
   * @return the book being borrowed
   */
  Optional<Book> borrowBook(String isbn);

  /**
   * Return a borrowed by
   *
   * @param isbn the isbn of the book being returned
   */
  void returnBook(String isbn);
}
