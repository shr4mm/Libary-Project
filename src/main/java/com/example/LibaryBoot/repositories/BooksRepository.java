package com.example.LibaryBoot.repositories;


import com.example.LibaryBoot.models.Book;
import com.example.LibaryBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);
    @Query("SELECT b FROM Book b WHERE b.name LIKE %:like%")
    List<Book> findLikeBookByName(@Param("like") String name);
    @Query("SELECT b FROM Book b WHERE b.author LIKE %:like%")
    List<Book> findLikeBookByAuthor(@Param("like") String author);
    @Query("SELECT b FROM Book b WHERE b.yearOfProduction = :year")
    List<Book> findBookByYearOfProduction(@Param("year") int year);

    @Query("SELECT b FROM Book b WHERE b.order IS NOT NULL")
    List<Book> booksForWhichThereAreOrders();
    @Modifying
    @Query("UPDATE Book b SET b.order = :order WHERE b.id = :bookId")
    void setOrder(@Param("order") Person order, @Param("bookId") int bookId);
    @Modifying
    @Query("UPDATE Book b SET b.order = null WHERE b.id = :bookId")
    void deleteOrder(@Param("bookId") int bookId);
    @Query("SELECT b.owner FROM Book b WHERE b.id = :bookId")
    Person findOwnerByBookId(@Param("bookId") int bookId);
    @Modifying
    @Query("UPDATE Book b SET b.owner = :owner WHERE b.id = :bookId")
    void setOwnerForBook(@Param("bookId") int bookId, @Param("owner") Person owner);
    @Modifying
    @Query("UPDATE Book b SET b.owner = null WHERE b.id = :bookId")
    void removeOwnerForBookById(@Param("bookId") int bookId);
    @Query("SELECT b FROM Book b WHERE b.name LIKE %:like%")
    List <Book> findBookByNameContains(@Param("like") String like);
    @Query("SELECT b FROM  Book b WHERE b.name = :name")
    Optional <Book> findByName(@Param("name") String name);
}
