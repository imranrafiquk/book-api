package com.techdelity.identitye2e.demo.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techdelity.identitye2e.demo.library.exception.AlreadyExistsException;
import com.techdelity.identitye2e.demo.library.exception.BookNotFoundException;
import com.techdelity.identitye2e.demo.library.model.Book;
import com.techdelity.identitye2e.demo.library.service.LibraryService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  LibraryService libraryService;

  private static final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    reset();
  }

  @Test
  void addNewBook_isCreated() throws Exception {
    this.mockMvc.perform(post("/api/book").contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(generateBook("12")))).andExpect(status().isCreated());
  }

  @Test
  void addDuplicateBook_isBadRequest() throws Exception {
    doThrow(new AlreadyExistsException("")).when(libraryService).addBook(generateBook("12"));
    this.mockMvc.perform(post("/api/book").contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(generateBook("12")))).andExpect(status().isBadRequest());
  }

  @Test
  void removeNonExistingBook_IsNotFound() throws Exception {
    doThrow(new BookNotFoundException()).when(libraryService).removeBook("12");
    this.mockMvc.perform(delete("/api/book/12").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void removeExistingBook_IsOk() throws Exception {
    this.mockMvc.perform(delete("/api/book/12").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void getBook_forExistingBook_isOk() throws Exception {
    when(libraryService.findBookByISBN("12")).thenReturn(Optional.of(generateBook("12")));
    this.mockMvc.perform(get("/api/book/findByISBN/12")).andExpect(status().isOk());
  }

  @Test
  void getBook_forExistingBook_getsBook() throws Exception {
    Book mockBook = generateBook("100");
    when(libraryService.findBookByISBN(mockBook.getIsbn())).thenReturn(
        Optional.of(generateBook(mockBook.getIsbn())));
    this.mockMvc.perform(get("/api/book/findByISBN/100"))
        .andExpect(jsonPath("isbn").value(mockBook.getIsbn()))
        .andExpect(jsonPath("author").value(mockBook.getAuthor()))
        .andExpect(jsonPath("title").value(mockBook.getTitle()))
        .andExpect(jsonPath("publicationYear").value(mockBook.getPublicationYear()))
        .andExpect(jsonPath("copiesAvailable").value(mockBook.getCopiesAvailable()));
  }

  @Test
  void getBook_forNonExistingBook_isNotFoundStatus() throws Exception {
    when(libraryService.findBookByISBN("123")).thenReturn(Optional.empty());
    this.mockMvc.perform(get("/api/book/findByISBN/123")).andExpect(status().isNotFound());
  }

  @Test
  void getBookByAuthor_noneFound_isNotFoundStatus() throws Exception {
    when(libraryService.findBookByAuthor("AuthorName")).thenReturn(Collections.emptyList());
    this.mockMvc.perform(get("/api/book/findByAuthor/AuthorName"))
        .andExpect(status().isNoContent());
  }

  @Test
  void getBookByAuthor_oneFound_isOk() throws Exception {
    when(libraryService.findBookByAuthor("AuthorName")).thenReturn(List.of(generateBook("12")));
    this.mockMvc.perform(get("/api/book/findByAuthor/AuthorName")).andExpect(status().isOk());
  }

  @Test
  void getBookByAuthor_oneFound_isCorrectBook() throws Exception {
    Book mockBook = generateBook("100");
    when(libraryService.findBookByAuthor(mockBook.getAuthor())).thenReturn(List.of(mockBook));
    this.mockMvc.perform(get("/api/book/findByAuthor/" + mockBook.getAuthor()))
        .andExpect(jsonPath("$.[0].isbn").value(mockBook.getIsbn()))
        .andExpect(jsonPath("$.[0].author").value(mockBook.getAuthor()))
        .andExpect(jsonPath("$.[0].title").value(mockBook.getTitle()))
        .andExpect(jsonPath("$.[0].publicationYear").value(mockBook.getPublicationYear()))
        .andExpect(
            jsonPath("$.[0].copiesAvailable").value(mockBook.getCopiesAvailable()));
  }

  @Test
  void getBookByAuthor_manyFound_areCorrectBooks() throws Exception {
    Book mockBook1 = generateBookWithSameAuthor("100");
    Book mockBook2 = generateBookWithSameAuthor("101");
    Book mockBook3 = generateBookWithSameAuthor("102");
    Book mockBook4 = generateBookWithSameAuthor("103");
    when(libraryService.findBookByAuthor(mockBook1.getAuthor())).thenReturn(
        List.of(mockBook1, mockBook2, mockBook3, mockBook4));
    this.mockMvc.perform(get("/api/book/findByAuthor/" + mockBook1.getAuthor()))
        .andExpect(jsonPath("$.[0].isbn").value(mockBook1.getIsbn()))
        .andExpect(jsonPath("$.[0].author").value(mockBook1.getAuthor()))
        .andExpect(jsonPath("$.[0].title").value(mockBook1.getTitle()))
        .andExpect(jsonPath("$.[0].publicationYear").value(mockBook1.getPublicationYear()))
        .andExpect(
            jsonPath("$.[0].copiesAvailable").value(mockBook1.getCopiesAvailable()))

        .andExpect(jsonPath("$.[1].isbn").value(mockBook2.getIsbn()))
        .andExpect(jsonPath("$.[1].author").value(mockBook2.getAuthor()))
        .andExpect(jsonPath("$.[1].title").value(mockBook2.getTitle()))
        .andExpect(jsonPath("$.[1].publicationYear").value(mockBook2.getPublicationYear()))
        .andExpect(
            jsonPath("$.[0].copiesAvailable").value(mockBook2.getCopiesAvailable()))

        .andExpect(jsonPath("$.[2].isbn").value(mockBook3.getIsbn()))
        .andExpect(jsonPath("$.[2].author").value(mockBook3.getAuthor()))
        .andExpect(jsonPath("$.[2].title").value(mockBook3.getTitle()))
        .andExpect(jsonPath("$.[2].publicationYear").value(mockBook3.getPublicationYear()))
        .andExpect(
            jsonPath("$.[2].copiesAvailable").value(mockBook3.getCopiesAvailable()))

        .andExpect(jsonPath("$.[3].isbn").value(mockBook4.getIsbn()))
        .andExpect(jsonPath("$.[3].author").value(mockBook4.getAuthor()))
        .andExpect(jsonPath("$.[3].title").value(mockBook4.getTitle()))
        .andExpect(jsonPath("$.[3].publicationYear").value(mockBook4.getPublicationYear()))
        .andExpect(
            jsonPath("$.[0].copiesAvailable").value(mockBook4.getCopiesAvailable()));
  }

  @Test
  void borrowBook_notFound_isNotFoundStatus() throws Exception {
    Book mockBook = generateBook("123");
    when(libraryService.borrowBook("123")).thenReturn(Optional.empty());
    this.mockMvc.perform(post("/api/book/borrow/" + mockBook.getIsbn()))
        .andExpect(status().isNotFound());
  }

  @Test
  void borrowBook_found_isOk() throws Exception {
    Book mockBook = generateBook("123");
    when(libraryService.borrowBook(mockBook.getIsbn())).thenReturn(Optional.of(mockBook));
    this.mockMvc.perform(post("/api/book/borrow/" + mockBook.getIsbn())).andExpect(status().isOk());
  }

  @Test
  void borrowBook_found_returnsBook() throws Exception {
    Book mockBook = generateBook("100");

    when(libraryService.borrowBook(mockBook.getIsbn())).thenReturn(Optional.of(mockBook));
    this.mockMvc.perform(post("/api/book/borrow/" + mockBook.getIsbn()))
        .andExpect(jsonPath("isbn").value(mockBook.getIsbn()))
        .andExpect(jsonPath("author").value(mockBook.getAuthor()))
        .andExpect(jsonPath("title").value(mockBook.getTitle()))
        .andExpect(jsonPath("publicationYear").value(mockBook.getPublicationYear()))
        .andExpect(jsonPath("copiesAvailable").value(mockBook.getCopiesAvailable()));

  }

  @Test
  void returnBook_notFound_isNotFoundStatus() throws Exception {
    Book mockBook = generateBook("100");
    doThrow(new BookNotFoundException()).when(libraryService).returnBook(mockBook.getIsbn());

    this.mockMvc.perform(post("/api/book/return/" + mockBook.getIsbn()))
        .andExpect(status().isNotFound());


  }

  @Test
  void seedBooks_addsBooks() throws Exception {

    this.mockMvc.perform(post("/api/book/seed"))
        .andExpect(status().isOk());


  }

  @Test
  void returnExistingBook_isOk() throws Exception {
    this.mockMvc.perform(post("/api/book/return/1")).andExpect(status().isOk());

  }

  static Book generateBook(String id) {
    return new Book(id, "Title:" + id, "Author:" + id, 2001, 1);
  }

  static Book generateBookWithSameAuthor(String id) {
    return new Book(id, "Title:" + id, "MissAuthor", 2001, 1);
  }
}