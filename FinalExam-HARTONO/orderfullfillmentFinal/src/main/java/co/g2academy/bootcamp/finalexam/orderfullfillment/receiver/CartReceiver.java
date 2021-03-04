/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.orderfullfillment.receiver;

import co.g2academy.bootcamp.finalexam.orderfullfillment.entity.Order;
import co.g2academy.bootcamp.finalexam.orderfullfillment.model.Cart;
import co.g2academy.bootcamp.finalexam.orderfullfillment.model.Converter;
import co.g2academy.bootcamp.finalexam.orderfullfillment.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author cimiko
 */
@RabbitListener(queues = "final_project")
@Component
public class CartReceiver {
    
    private static final Logger LOG = LoggerFactory.getLogger(CartReceiver.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Converter converter = new Converter();
    
    @RabbitHandler
    public void receive(byte[] message){
        try {
            String messageBody = new String (message);
            Cart cart = objectMapper.readValue(messageBody, Cart.class);
            Order order = converter.convert(cart);
            orderRepository.save(order);
        } catch (JsonProcessingException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
    
}
