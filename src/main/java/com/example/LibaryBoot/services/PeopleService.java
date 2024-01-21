package com.example.LibaryBoot.services;


import com.example.LibaryBoot.models.Book;
import com.example.LibaryBoot.models.Person;
import com.example.LibaryBoot.repositories.PeopleRepository;
import com.example.LibaryBoot.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final PeopleRepository peopleRepository;
    @Autowired
    @Lazy
    public PeopleService(PasswordEncoder passwordEncoder, PeopleRepository peopleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.peopleRepository = peopleRepository;
    }
    public List<Person> findAll(){
        return peopleRepository.findAll();
    }
    public Person findOne(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }
    @Transactional
    public void save(Person person){
        person.setRole("ROLE_USER");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }
    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        updatedPerson.setRole(peopleRepository.findById(id).get().getRole());
        updatedPerson.setPassword(deteilsPerson().getPassword());
        peopleRepository.save(updatedPerson);
    }
    @Transactional
    public void updatePassword(Person updatedPerson){
        String password = updatedPerson.getPassword();
        updatedPerson = deteilsPerson();
        updatedPerson.setPassword(passwordEncoder.encode(password));
        peopleRepository.save(updatedPerson);
    }
    public Optional<Person> findByName(String name){
        return peopleRepository.findByName(name);
    }
    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }
    @Transactional
    public List<Book> findBooksByPerson(Person person){
        return peopleRepository.findBooksByPerson(person);
    }
    @Transactional
    public List<Book> findPersonOrders (Person person){
        return peopleRepository.findPersonOrders(person);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByName(username);
        if(person.isEmpty()){
            throw new UsernameNotFoundException("Not found");
        }else {
            return new PersonDetails(person.get());
        }
    }
    public Person deteilsPerson(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails details = (PersonDetails) authentication.getPrincipal();
        return details.getPerson();
    }
}

