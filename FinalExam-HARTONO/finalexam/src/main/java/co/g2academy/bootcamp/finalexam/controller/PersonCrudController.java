/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.controller;

import co.g2academy.bootcamp.finalexam.Repository.PersonRepository;
import co.g2academy.bootcamp.finalexam.entity.Person;
import co.g2academy.bootcamp.finalexam.model.RegisterValidation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cimiko
 */
@RestController
@RequestMapping("/api")
public class PersonCrudController {
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private RegisterValidation validation;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @GetMapping("/persons")
    public List<Person> getPersons(){
        return personRepository.findAll();
    }
    
    @GetMapping("/person/id/{id}")
    public Person getPerson(@PathVariable Integer id){
        return personRepository.findById(id).get();
    }
    
    @GetMapping("/person/user-name/{userName}")
    public Person getPerson(@PathVariable String userName){
        return personRepository.findByuserName(userName);
    }
    
    @PostMapping("/person")
    public Person save(@RequestBody Person p){
        if(validation.validateEmail(p)){
            p.setPassword(bCryptPasswordEncoder.encode(p.getPassword()));
            personRepository.save(p);
            return personRepository.findByuserName(p.getUserName());
        }
        return null;
    }
    
    @PutMapping("/person/{id}")
    public Person update(@RequestBody Person p){
        p.setPassword(bCryptPasswordEncoder.encode(p.getPassword()));
        return personRepository.save(p);
    }
    
    @DeleteMapping("/person/{id}")
    public Person delete(@PathVariable Integer id){
        Person p = personRepository.findById(id).get();
        if(p != null){
            personRepository.delete(p);
            return p;
        }
        return p;
    }
    
    
}
