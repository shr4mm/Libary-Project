package com.example.LibaryBoot.controllers;


import com.example.LibaryBoot.models.Book;
import com.example.LibaryBoot.models.Person;
import com.example.LibaryBoot.security.PersonDetails;
import com.example.LibaryBoot.services.BooksService;
import com.example.LibaryBoot.services.PeopleService;
import com.example.LibaryBoot.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final PeopleService peopleService;
    private final BooksService booksService;
    private final BookValidator validator;

    @Autowired
    public BookController(PeopleService peopleService, BooksService booksService, BookValidator validator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.validator = validator;
    }

    @GetMapping
    public String showAll(Model model, @RequestParam(name = "sort_by_year_of_production", defaultValue = "false") boolean sortByYear) {
        List<Book> books;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();
        if (sortByYear) {
            books = booksService.findAllSortedByYearOfProduction();
        } else {
            books = booksService.findAll();
        }
        model.addAttribute("role", person.getRole());
        model.addAttribute("books", books);
        return "books/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Person personn = peopleService.deteilsPerson();
        model.addAttribute("book", booksService.findOne(id));
        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("owner", booksService.findOwnerByBookId(id));
        model.addAttribute("role", personn.getRole());
        model.addAttribute("personn", personn);
        model.addAttribute("order", booksService.findOne(id).getOrder());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/admin/new";
    }

    @PostMapping
    public String save(@ModelAttribute("book")@Valid Book book, BindingResult bindingResult) {
        validator.validate(book, bindingResult);
        if(bindingResult.hasErrors()){
            return "books/admin/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/admin/edit";
    }

    @PatchMapping("/{id}")
    public String editSave(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/admin/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }


    @PatchMapping("/{id}/add_owner")
    public String addOwner(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.setOwnerForBook(id, person);
        booksService.deleteOrder(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/delete_owner")
    public String deleteOwner(@PathVariable("id") int id) {
        booksService.removeOwnerForBookById(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchPage(Model model, @RequestParam(name = "like", defaultValue = "&&&&&") String like) {
        List<Book> books = booksService.findBooksByNameContains(like);
        Book book = null;
        if(!books.isEmpty()) {
            book = books.get(0);
        }
            if (book == null) {
                model.addAttribute("bookNull", true);
            } else {
                model.addAttribute("findBook", books.get(0));
                model.addAttribute("owner", booksService.findOwnerByBookId(book.getId()));
                model.addAttribute("personDetails", peopleService.deteilsPerson());
            }
        if (!like.equals("&&&&&")) {
            model.addAttribute("search", true);
        }
        model.addAttribute("like", like);
        return "books/search";
    }

    @PostMapping("/order/{id}")
    public String saveOrder(@PathVariable("id") int personId, @RequestParam(name = "bookId", defaultValue = "") int bookId){
        booksService.setOrder(personId, bookId);
        return "redirect:/books";
    }

    @DeleteMapping ("/order/{id}")
    public String deleteOrder(@PathVariable("id") int bookId){
        booksService.deleteOrder(bookId);
        return "redirect:/books";
    }

    @GetMapping("/show_orders")
    public String showAllOrdersForAdmin(Model model){
        model.addAttribute("allOrders", booksService.BooksForWhichThereAreOrder());
        return "books/admin/showOrders";
    }
}
