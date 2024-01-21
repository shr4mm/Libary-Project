package com.example.LibaryBoot.controllers;

import com.example.LibaryBoot.models.Person;
import com.example.LibaryBoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    private final PeopleService peopleService;
    @Autowired
    public HelloController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    public String hello(Model model){
        Person person = peopleService.deteilsPerson();
        model.addAttribute("role", person.getRole());
        model.addAttribute("person", person);
        return "hello/hello";
    }
}

