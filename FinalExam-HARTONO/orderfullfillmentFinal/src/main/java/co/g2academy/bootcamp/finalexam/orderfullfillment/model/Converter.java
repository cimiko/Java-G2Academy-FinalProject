/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.g2academy.bootcamp.finalexam.orderfullfillment.model;

import co.g2academy.bootcamp.finalexam.orderfullfillment.entity.Order;
import co.g2academy.bootcamp.finalexam.orderfullfillment.entity.OrderItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cimiko
 */
public class Converter {
    
    public Order convert(Cart cart){
        
        Order order = new Order();
        order.setStatus("PROCESSED");
        order.setPersonId(cart.getPerson().getId());
        order.setCartId(cart.getId());
        order.setOrderDate(new Date());
        List<OrderItem> orderItems = new ArrayList<>();
        order.setItems(orderItems);
        Integer totalPrice = 0;
        
        for(CartItem item : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(item.getId());
            orderItem.setPrice(item.getPrice());
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
            totalPrice += item.getPrice() * item.getQuantity();
        }
        order.setTotalPrice(totalPrice);
        return order;
    }
    
}
