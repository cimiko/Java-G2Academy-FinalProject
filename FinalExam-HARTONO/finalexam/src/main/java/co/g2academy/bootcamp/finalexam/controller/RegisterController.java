/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.controller;

import co.g2academy.bootcamp.finalexam.Repository.PersonRepository;
import co.g2academy.bootcamp.finalexam.entity.Person;
import co.g2academy.bootcamp.finalexam.model.Register;
import co.g2academy.bootcamp.finalexam.model.RegisterValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cimiko
 */
@RestController
@RequestMapping("/api")
public class RegisterController {
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private RegisterValidation validator;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @PostMapping("/register")
    public String register(@RequestBody Register r){
        if(validator.validate(r)){
            Person p = personRepository.findByuserName(r.getUserName());
            
            if(p == null){
                Person newPerson = new Person();
                newPerson.setUserName(r.getUserName());
                newPerson.setName(r.getName());
                newPerson.setPassword(bCryptPasswordEncoder.encode(r.getPassword()));
                personRepository.save(newPerson);
                return "Register Success";
            }
        }
        return "Register Failed";
    }
    
}
