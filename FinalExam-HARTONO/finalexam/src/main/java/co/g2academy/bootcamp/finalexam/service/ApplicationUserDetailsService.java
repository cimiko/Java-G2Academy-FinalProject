/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.service;

import co.g2academy.bootcamp.finalexam.Repository.PersonRepository;
import co.g2academy.bootcamp.finalexam.entity.Person;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author cimiko
 */
@Service
public class ApplicationUserDetailsService implements UserDetailsService{
    
    @Autowired
    private PersonRepository personRepository;
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        Person person = personRepository.findByuserName(userName);
        if(person == null){
            throw new UsernameNotFoundException(userName);
        }
        return new User(person.getUserName(), person.getPassword(), Collections.emptyList());
    }
    
}
