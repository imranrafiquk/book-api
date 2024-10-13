package com.techdelity.identitye2e.demo.library.repository;

import com.techdelity.identitye2e.demo.library.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

  List<Book> findByAuthor(String author);

}
