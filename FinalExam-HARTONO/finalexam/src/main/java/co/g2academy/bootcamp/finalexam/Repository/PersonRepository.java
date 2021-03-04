/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.Repository;

import co.g2academy.bootcamp.finalexam.entity.Person;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cimiko
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, Object>{
    
    Person findByuserName(String userName);
    
    Person findByuserName(Person person);
    
    List<Person> findAll();
    
}
