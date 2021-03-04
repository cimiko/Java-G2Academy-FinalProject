/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.controller;

import co.g2academy.bootcamp.finalexam.entity.Product;
import co.g2academy.bootcamp.finalexam.model.AddToCart;
import co.g2academy.bootcamp.finalexam.service.CartService;
import io.jsonwebtoken.Claims;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add-to-cart")
    public String addToCart(
            @RequestBody AddToCart addToCart,
            Principal principal){
        
//        Product product = new Product();
//        AddToCart addCart = new AddToCart();
//        
//        Integer stock = product.getStock() - addCart.getQuantity();
////        if(product.getStock() > 0){
////            
////        }
//        product.setStock(stock);
        
        cartService.addToCart(getUserName(principal), addToCart);
        return "ok";
    }
    
    @PostMapping("/cart")
    public String checkout(Principal principal){
//        Product product = new Product();
//        AddToCart addCart = new AddToCart();
//        
//        Integer stock = product.getStock() - addCart.getQuantity();
////        if(product.getStock() > 0){
////            
////        }
//        product.setStock(stock);
        
        cartService.cart(getUserName(principal));
        
        return "ok";
    }
    
    private  String getUserName(Principal principal){
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        Claims user = (Claims) token.getPrincipal();
        return user.getSubject();
    }
    
}
