package com.bhargrah.orders.events.handler;

import com.bhargrah.orders.events.OrderCreatedEvent;
import com.bhargrah.orders.repositories.OrdersRepository;
import com.bhargrah.orders.repositories.entity.OrderEntity;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    OrdersRepository ordersRepository;

    @Autowired
    public OrderEventHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @EventHandler
    public void handle(OrderCreatedEvent orderCreatedEvent){

        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderCreatedEvent,orderEntity);

        ordersRepository.save(orderEntity);
    }
}
