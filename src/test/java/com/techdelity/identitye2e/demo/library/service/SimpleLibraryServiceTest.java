package com.techdelity.identitye2e.demo.library.service;

import com.techdelity.identitye2e.demo.library.exception.AlreadyExistsException;
import com.techdelity.identitye2e.demo.library.exception.BookNotFoundException;
import com.techdelity.identitye2e.demo.library.exception.NoBookCopiesRemainingToBorrowException;
import com.techdelity.identitye2e.demo.library.model.Book;
import com.techdelity.identitye2e.demo.library.repository.BookRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleLibraryServiceTest {

  private LibraryService libraryService;

  @Mock
  private BookRepository bookRepository;

  @BeforeEach
  void setUp() {
    libraryService = new SimpleLibraryService(bookRepository);

  }

  @Test
  void addingBook_savesBook() {
    Book book = generateBook("12");
    when(bookRepository.save(any(Book.class))).thenReturn(book);
    assertEquals(book, libraryService.addBook(book));
  }

  @Test
  void addingTheSameBookTwice_throwsException() {
    Book book = generateBook("12");
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));
    assertThrows(AlreadyExistsException.class, () -> libraryService.addBook(book));
  }

  @Test
  void removingBook_deletesBook() {
    when(bookRepository.findById("4")).thenReturn(Optional.of(new Book()));
    assertDoesNotThrow(() -> libraryService.removeBook("4"));
  }

  @Test
  void removingNonExistingBook_throwsException() {
    when(bookRepository.findById("8")).thenReturn(Optional.empty());
    assertThrows(BookNotFoundException.class, () -> libraryService.removeBook("8"));
  }

  @Test
  void findBookByIsbn_bookDoesNotExists_returnsEmpty() {
    assertEquals(Optional.empty(), libraryService.findBookByISBN("1"));
  }

  @Test
  void findBookByIsbn_bookExists_returnsBook() {
    Book book = generateBook("12");
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));
    assertEquals(Optional.of(book), libraryService.findBookByISBN(book.getIsbn()));
  }

  @Test
  void findBookByIsbn_multipleBookExists_returnsCorrectBook() {
    Book book = generateBook("12");
    libraryService.addBook(book);
    libraryService.addBook(generateBook("1"));
    libraryService.addBook(generateBook("2"));
    libraryService.addBook(generateBook("3"));
    libraryService.addBook(generateBook("4"));
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));

    assertEquals(Optional.of(book), libraryService.findBookByISBN(book.getIsbn()));
  }

  @Test
  void findBookByAuthor_noBooksMatchAuthor_returnsEmptyList() {
    Book book = generateBook("12");
    libraryService.addBook(book);
    libraryService.addBook(generateBook("1"));
    libraryService.addBook(generateBook("2"));
    libraryService.addBook(generateBook("3"));
    libraryService.addBook(generateBook("4"));
    assertEquals(List.of(), libraryService.findBookByAuthor("Author:9"));
  }

  @Test
  void findBookByAuthor_oneBookMatchesAuthor_returnsSingleItemList() {
    Book book = generateBook("12");
    libraryService.addBook(book);
    libraryService.addBook(generateBook("1"));
    libraryService.addBook(generateBook("2"));
    libraryService.addBook(generateBook("3"));
    libraryService.addBook(generateBook("4"));
    when(bookRepository.findByAuthor(book.getAuthor())).thenReturn(Collections.singletonList(book));

    assertEquals(List.of(book), libraryService.findBookByAuthor(book.getAuthor()));
  }

  @Test
  void findBookByAuthor_manyBooksMatchesAuthor_returnsSingleItemList() {
    String mrAuthor = "Mr Author";
    Book book1 = generateBook("12", mrAuthor);
    Book book2 = generateBook("13", mrAuthor);
    Book book3 = generateBook("14", mrAuthor);
    libraryService.addBook(book1);
    libraryService.addBook(book2);
    libraryService.addBook(book3);
    libraryService.addBook(generateBook("3"));
    libraryService.addBook(generateBook("4"));
    when(bookRepository.findByAuthor(mrAuthor)).thenReturn(List.of(book1, book2, book3));

    assertEquals(List.of(book1, book2, book3), libraryService.findBookByAuthor(mrAuthor));
  }

  @Test
  void borrowUnknownBook_ThrowsException() {
    Book book = generateBook(10);
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.empty());
    assertEquals(Optional.empty(), libraryService.borrowBook(book.getIsbn()));
  }

  @Test
  void borrowBook_returnsCorrectBook() {
    Book book = generateBook(10);
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));
    assertEquals(Optional.of(book), libraryService.borrowBook(book.getIsbn()));
  }

  @Test
  void borrowBook_updatesCopiesRemaining() {
    Book book = generateBook(10);
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));
    assertEquals(Optional.of(book), libraryService.borrowBook(book.getIsbn()));
    libraryService.findBookByISBN(book.getIsbn()).map(Book::getCopiesAvailable)
        .ifPresentOrElse(copiesRemaining -> assertEquals(9, copiesRemaining.intValue()), Assertions::fail);
  }

  @Test
  void borrowAllCopiesBook_updatesCopiesRemainingToZero() {
    int totalCopies = 5;
    Book book = generateBook(totalCopies);
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));

    for (int i = 0; i < totalCopies; i++) {
      assertEquals(Optional.of(book), libraryService.borrowBook(book.getIsbn()));
    }
    libraryService.findBookByISBN(book.getIsbn()).map(Book::getCopiesAvailable)
        .ifPresentOrElse(copiesRemaining -> assertEquals(0, copiesRemaining.intValue()), Assertions::fail);
  }

  @Test
  void borrowMoreCopiesBookThanAvailable_ThrowsException() {
    Book book = generateBook(0);
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));
    String isbn = book.getIsbn();
    assertThrows(NoBookCopiesRemainingToBorrowException.class,
        () -> libraryService.borrowBook(isbn));
  }

  @Test
  void returningUnknownBook_ThrowsException() {
    Book book = generateBook(1);
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.empty());
    String isbn = book.getIsbn();
    assertThrows(BookNotFoundException.class, ()-> libraryService.returnBook(isbn));
  }

  @Test
  void returningBook_UpdatesCopiesAvailable() {
    Book book = generateBook(1);
    when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));
    libraryService.returnBook(book.getIsbn());
    libraryService.findBookByISBN(book.getIsbn()).map(Book::getCopiesAvailable)
        .ifPresentOrElse(copiesRemaining -> assertEquals(2, copiesRemaining.intValue()), Assertions::fail);
  }

  static Book generateBook(String id) {
    return new Book(id, "Title:" + id, "Author:" + id, 2001, 1);
  }

  static Book generateBook(String id, String author) {
    return new Book(id, "Title:" + id, author, 2001, 1);
  }

  static Book generateBook(int copies) {
    return new Book("123", "Title", "Author", 2001, copies);
  }

}