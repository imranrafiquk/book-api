package com.techdelity.identitye2e.demo.library.controller;

import com.techdelity.identitye2e.demo.library.model.Book;
import com.techdelity.identitye2e.demo.library.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/book")
public class LibraryController {

  private final LibraryService libraryService;

  @Autowired
  public LibraryController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @Operation(summary = "Add a new book", description = "Add a new book to the library")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created new book"),
      @ApiResponse(responseCode = "400", description = "Error creating new book")})
  @PostMapping
  public ResponseEntity<String> addBook(@RequestBody Book book) {
    libraryService.addBook(book);
    return ResponseEntity.status(HttpStatus.CREATED).body("Book successfully saved");
  }

  @Operation(summary = "Delete book", description = "Remove a book from the library")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully deleted book"),
      @ApiResponse(responseCode = "400", description = "Book not found")})
  @DeleteMapping("/{isbn}")
  public ResponseEntity<String> removeBook(@PathVariable String isbn) {
    libraryService.removeBook(isbn);
    return ResponseEntity.status(HttpStatus.OK).body("Book successfully saved");
  }

  @Operation(summary = "Get a book by the ISBN", description = "Find a book in the library by the ISBN")
  @GetMapping("/findByISBN/{isbn}")
  public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
    return ResponseEntity.of(libraryService.findBookByISBN(isbn));
  }

  @Operation(summary = "Get a book by the ISBN", description = "Find a book in the library by the ISBN")
  @GetMapping("/findByAuthor/{author}")
  public ResponseEntity<List<Book>> getBookByAuthor(@PathVariable String author) {
    List<Book> results = libraryService.findBookByAuthor(author);
    return ResponseEntity.status(results.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
        .body(results);

  }

  @Operation(summary = "Borrow a book by the ISBN", description = "Borrow a book")
  @PostMapping("/borrow/{isbn}")
  public ResponseEntity<Book> borrowBook(@PathVariable String isbn) {
    return ResponseEntity.of(libraryService.borrowBook(isbn));

  }

  @Operation(summary = "Return a book", description = "Return a borrowed a book")
  @PostMapping("/return/{isbn}")
  public ResponseEntity<String> returnBook(@PathVariable String isbn) {
    libraryService.returnBook(isbn);
    return ResponseEntity.ok("Book returned");

  }

  @Operation(summary = "DEBUG Seed some books", description = """
      | ISBN               | Title                                        | Author                         | Publication Year | Copies |
      |--------------------|----------------------------------------------|--------------------------------|-------------------|--------|
      | 978-1-61729-008-1  | Cracking the Coding Interview                 | Gayle Laakmann McDowell       | 2021              | 5      |
      | 978-0-321-54573-0  | Elements of Programming Interviews            | Adnan Aziz                    | 2012              | 3      |
      | 978-0-9961281-0-3  | System Design Interview – An Insider's Guide  | Alex Xu                       | 2020              | 4      |
      | 978-1-59327-000-1  | The Geek's Guide to Interviews                | T. D. Pankaj                  | 2021              | 5      |
      """)
  @PostMapping("/seed")
  public ResponseEntity<String> seedBooks() {
    libraryService.addBook(
        new Book("978-1-61729-008-1", "Cracking the Coding Interview", "Gayle Laakmann McDowell",
            2021, 5));
    libraryService.addBook(
        new Book("978-0-321-54573-0", "Elements of Programming Interviews", "Adnan Aziz", 2012, 3));
    libraryService.addBook(
        new Book("978-0-9961281-0-3", "System Design Interview – An Insider's Guide", "Alex Xu",
            2020, 4));

    return ResponseEntity.ok("Books seeded");

  }


}
