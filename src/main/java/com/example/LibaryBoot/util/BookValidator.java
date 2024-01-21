package com.example.LibaryBoot.util;


import com.example.LibaryBoot.models.Book;
import com.example.LibaryBoot.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class BookValidator implements Validator {

    private final BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        Optional<Book> existingBook = booksService.findBookByName(book.getName());
        if (existingBook.isPresent()) {
            errors.rejectValue("name", "", "This name is already taken");
        }
    }
}

