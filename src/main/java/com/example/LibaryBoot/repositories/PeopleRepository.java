package com.example.LibaryBoot.repositories;


import com.example.LibaryBoot.models.Book;
import com.example.LibaryBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    @Query("SELECT b FROM Book b WHERE b.owner = :person")
    List<Book> findBooksByPerson(@Param("person") Person person);
    @Query("SELECT b FROM Book b WHERE b.order = :person")
    List<Book> findPersonOrders(@Param("person") Person person);
    Optional <Person> findByName (String name);
}
