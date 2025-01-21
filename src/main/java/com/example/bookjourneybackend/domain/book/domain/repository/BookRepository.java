package com.example.bookjourneybackend.domain.book.domain.repository;

import com.example.bookjourneybackend.domain.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbnCode(String isbn);
}
