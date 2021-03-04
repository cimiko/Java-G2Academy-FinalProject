/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.controller;

import co.g2academy.bootcamp.finalexam.Repository.CachedProductRepository;
import co.g2academy.bootcamp.finalexam.Repository.PersonRepository;
import co.g2academy.bootcamp.finalexam.Repository.ProductRepository;
import co.g2academy.bootcamp.finalexam.entity.Person;
import co.g2academy.bootcamp.finalexam.entity.Product;
import io.jsonwebtoken.Claims;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cimiko
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CachedProductRepository cachedProductRepository;

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/product/id/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Integer productId) {
        try {
            Product p = productRepository.findById(productId).get();
            return ResponseEntity.ok(p);
        } catch (Exception e) {

        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getProductBySearchQuery(
            @RequestParam String query,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort) {

        return buildResponseEntity(cachedProductRepository.findByNameContaining(query, page, size, sort));
    }

    @GetMapping("/product")
    public ResponseEntity<Map<String, Object>> getProductByCategory(
            @RequestParam String category,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort) {

        return buildResponseEntity(cachedProductRepository.findByCategory(category, page, size, sort));
    }

    @PostMapping("/product")
    public ResponseEntity<String> save(
            @RequestBody Product product,
            Principal principal) {
        Person person = personRepository.findByuserName(getUserName(principal));
        if (person == null) {
            return ResponseEntity.ok("User is not found");
        }
        product.setPerson(person);
        productRepository.save(product);
        return ResponseEntity.ok("OK");
    }
   

    @PutMapping("/product/{productId}")
    public ResponseEntity<String> updateId(
            @PathVariable Integer productId,
            @RequestBody Product p,
            Principal principal) {
        
        Person person = personRepository.findByuserName(getUserName(principal));
        
        
        if(person == null){
            return ResponseEntity.ok("User is not found");
        }else if(person.equals(p.getPerson())){
            return ResponseEntity.ok("Product Error");
        }
        
//        Product p = productRepository.findByIdAndName(productId, person);
//        if(p == null){
//            return ResponseEntity.ok("Product is not found");
//        }
        p.setPerson(person);
        productRepository.save(p);
        return ResponseEntity.ok("Ok");
    }

    @PutMapping("/product/stock/{productId}")
    public ResponseEntity<String> updateStock(
            @PathVariable Integer productId,
            @RequestBody Integer stock,
            Principal principal) {

        Person person = personRepository.findByuserName(getUserName(principal));
        if (person == null) {
            return ResponseEntity.ok("User is not found");
        }
        Product p = productRepository.findByIdAndPerson(productId, person);
        if (p == null) {
            return ResponseEntity.ok("Product is not found");
        }
        p.setStock(stock);
        productRepository.save(p);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("product/{id}")
    public Product delete(@PathVariable Integer id) {
        Product product = productRepository.findById(id).get();
        if (product != null) {
            productRepository.delete(product);
            return product;
        }
        return product;
    }

    private String getUserName(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        Claims user = (Claims) token.getPrincipal();
        return user.getSubject();
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(Page<Product> productPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("product", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
