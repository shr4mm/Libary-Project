package com.example.LibaryBoot.controllers;

import com.example.LibaryBoot.models.Person;
import com.example.LibaryBoot.services.PeopleService;
import com.example.LibaryBoot.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private PersonValidator personValidator;
    private final PeopleService peopleService;
    @Autowired
    public AuthController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }
    @Autowired
    public void setPersonValidator(PersonValidator personValidator) {
        this.personValidator = personValidator;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String regPage(@ModelAttribute("person") Person person){

        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registrationNew(@ModelAttribute("person")@Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "auth/registration";
        }
        peopleService.save(person);
        return "redirect:/people";
    }
}
