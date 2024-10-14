package com.techdelity.identitye2e.demo.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.Version;

import lombok.Data;

import java.util.Objects;
import lombok.NoArgsConstructor;

/**
 * Book record
 */
@Data
@Entity
@NoArgsConstructor
public final class Book {

  @Id
  private String isbn;
  private String title;
  private String author;
  private int publicationYear;
  private int copiesAvailable;

  @Version
  private int version;

  public Book(String isbn, String title, String author, int publicationYear, int copiesAvailable) {
    this.isbn = isbn;
    this.title = title;
    this.author = author;
    this.publicationYear = publicationYear;
    this.copiesAvailable = copiesAvailable;
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == this) {
          return true;
      }
      if (obj == null || obj.getClass() != this.getClass()) {
          return false;
      }
    var that = (Book) obj;
    return Objects.equals(this.isbn,
        that.isbn) &&
        Objects.equals(this.version, that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isbn, version);
  }

}
