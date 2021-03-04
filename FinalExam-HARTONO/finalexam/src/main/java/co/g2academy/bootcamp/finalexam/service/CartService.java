/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.service;

import co.g2academy.bootcamp.finalexam.model.AddToCart;

/**
 *
 * @author cimiko
 */
public interface CartService {
    
    public void addToCart(String userName, AddToCart addToCart);
    
    public void cart(String userName);
    
}
