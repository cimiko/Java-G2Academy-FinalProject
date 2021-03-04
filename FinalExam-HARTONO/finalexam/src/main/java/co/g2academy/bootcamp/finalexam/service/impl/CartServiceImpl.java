/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.service.impl;

import co.g2academy.bootcamp.finalexam.AppConfig;
import co.g2academy.bootcamp.finalexam.Repository.CartRepository;
import co.g2academy.bootcamp.finalexam.Repository.PersonRepository;
import co.g2academy.bootcamp.finalexam.Repository.ProductRepository;
import co.g2academy.bootcamp.finalexam.entity.Cart;
import co.g2academy.bootcamp.finalexam.entity.CartItem;
import co.g2academy.bootcamp.finalexam.entity.Person;
import co.g2academy.bootcamp.finalexam.entity.Product;
import co.g2academy.bootcamp.finalexam.model.AddToCart;
import co.g2academy.bootcamp.finalexam.service.CartService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cimiko
 */
@Service
public class CartServiceImpl implements CartService {

    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public void addToCart(String userName, AddToCart addToCart) {
        Person person = personRepository.findByuserName(userName);

        Cart cart
                = cartRepository.findByPersonAndStatus(person, "ACTIVE");

        if (cart == null) {
            cart = new Cart();
            cart.setPerson(person);
            cart.setStatus("ACTIVE");
            List<CartItem> cartItems = new ArrayList<>();
            cart.setCartItems(cartItems);
        }
        Product product
                = productRepository.findById(addToCart.getProductId()).get();

        if (product != null) {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setPrice(product.getPrice());
            item.setQuantity(addToCart.getQuantity());
            item.setProduct(product);
            cart.getCartItems().add(item);

            Integer stock = product.getStock() - item.getQuantity();
            if (stock >= 0) {
                product.setStock(stock);
                productRepository.save(product);
                cartRepository.save(cart);
            }
            
        }
        
        

//        Product product = new Product();
//        AddToCart addCart = new AddToCart();
    }

    @Override
    @Transactional
    public void cart(String userName) {
        Person person = personRepository.findByuserName(userName);
        Cart cart
                = cartRepository.findByPersonAndStatus(person, "ACTIVE");

        if (cart != null) {
            cart.setStatus("PROCESSED");
            cart.setTransactionDate(new Date());

            LOG.info("Sending message to AMQP");
            rabbitTemplate.convertAndSend(AppConfig.QUEUE_NAME, cart);

            cartRepository.save(cart);
        }
    }
}
