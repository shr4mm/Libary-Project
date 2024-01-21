package com.example.LibaryBoot.controllers;

import com.example.LibaryBoot.models.Book;
import com.example.LibaryBoot.models.Person;
import com.example.LibaryBoot.services.BooksService;
import com.example.LibaryBoot.services.PeopleService;
import com.example.LibaryBoot.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/people")

public class PersonController {

    private final PeopleService peopleService;
    @Autowired
    public PersonController(PeopleService peopleService){
        this.peopleService = peopleService;
    }

    @GetMapping
    public String showAll(Model model){
        model.addAttribute("people", peopleService.findAll());
        return "people/admin/all";
    }

    @GetMapping("/{id}")
    public String showOne(Model model, @PathVariable("id") int id){
        Person person = peopleService.findOne(id);
        List<Book> books = peopleService.findBooksByPerson(person);
        List<Book> orders = peopleService.findPersonOrders(person);
        model.addAttribute("person", person);
        model.addAttribute("books", books);
        model.addAttribute("orders", orders);
        model.addAttribute("personDetails", peopleService.deteilsPerson());
        return "people/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        Person person = peopleService.findOne(id);
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("personDetails", peopleService.deteilsPerson());
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person")@Valid Person person,BindingResult bindingResult ,@PathVariable("id") int id){
        if(person.getId() != peopleService.deteilsPerson().getId()){
            return "util/nizya";
        }
        if(bindingResult.hasErrors()){
            return "people/edit";
        }
        peopleService.update(id, person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        peopleService.delete(id);
        return "redirect:/auth/login";
    }
    @GetMapping("/edit_pass")
    public String editPassPage (Model model){
        model.addAttribute("person", new Person());
        return "people/editPassword";
    }
    @PostMapping("/edit_password")
    public String saveNewPass(@ModelAttribute("person")@Valid Person person, BindingResult bindingResult){
        int id = peopleService.deteilsPerson().getId();
        if(bindingResult.hasFieldErrors("password")){
            return "people/editPassword";
        }
        System.out.println(person.getPassword());
        peopleService.updatePassword(person);
        return "redirect:/books";
    }

    //Вытягивание человека с авторизации
//    @GetMapping("/showUserInfo")
//    public String showUserInfo(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//        System.out.println(personDetails.getPerson().getName());
//        return "people/all";
//    }
}
