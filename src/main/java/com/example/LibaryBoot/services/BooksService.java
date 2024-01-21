package com.example.LibaryBoot.services;


import com.example.LibaryBoot.models.Book;
import com.example.LibaryBoot.models.Person;
import com.example.LibaryBoot.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final PeopleService peopleService;
    private final BooksRepository booksRepository;
    @Autowired
    public BooksService(PeopleService peopleService, BooksRepository booksRepository) {
        this.peopleService = peopleService;
        this.booksRepository = booksRepository;
    }
    public List<Book> findAll(){
        return booksRepository.findAll();
    }
    public Book findOne(int id){
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }
    public Optional <Book> findBookByName(String name) {
        return booksRepository.findByName(name);
    }
    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }
    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(int id){
        booksRepository.deleteById(id);
    }
    @Transactional
    public List<Book> findByOwner(Person person){
        return booksRepository.findByOwner(person);
    }
    @Transactional
    public void setOrder(int personId, int bookId){
        booksRepository.setOrder(peopleService.findOne(personId), bookId);
    }
    @Transactional
    public void deleteOrder(int bookId){
        booksRepository.deleteOrder(bookId);
    }
    @Transactional
    public Person findOwnerByBookId(int bookId){
        return booksRepository.findOwnerByBookId(bookId);
    }
    @Transactional
    public void setOwnerForBook(int bookId, Person owner){
        booksRepository.setOwnerForBook(bookId, owner);
    }
    @Transactional
    public void removeOwnerForBookById(int id){
        booksRepository.removeOwnerForBookById(id);
    }
    public List<Book> findAllSortedByYearOfProduction() {
        return booksRepository.findAll(Sort.by(Sort.Order.asc("yearOfProduction")));
    }
    public List<Book> BooksForWhichThereAreOrder(){
       return booksRepository.booksForWhichThereAreOrders();
    }
    public List<Book> findBooksByNameContains(String like) {
        return booksRepository.findBookByNameContains(like);
    }
}
