/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.model;

import co.g2academy.bootcamp.finalexam.entity.Person;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 *
 * @author cimiko
 */
@Component
public class RegisterValidation {
    
    public Boolean validate(Register model){
        return validateUserNameAsEmailAddress(model)
                && validatePasswordAndConfirmPasswordIsTheSame(model);
    }
    
    public Boolean validateUserNameAsEmailAddress(Register model){
        
        String regex = "^.(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(model.getUserName()).matches();
        
    }
    
    public Boolean validatePasswordAndConfirmPasswordIsTheSame(Register model){
        return model.getPassword() != null
                && model.getConfirmPassword() != null
                && model.getPassword().trim().length() > 0
                && model.getConfirmPassword().trim().length() > 0
                && model.getPassword().equals(model.getConfirmPassword());
    }
    
    public Boolean validateEmail(Person p){
        String regex = "^.(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(p.getUserName()).matches();
    }
    
}
