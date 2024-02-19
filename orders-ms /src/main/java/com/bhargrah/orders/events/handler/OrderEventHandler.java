package com.bhargrah.orders.events.handler;

import com.bhargrah.orders.events.OrderApprovedEvent;
import com.bhargrah.orders.repositories.OrdersRepository;
import com.bhargrah.orders.repositories.entity.OrderEntity;
import com.bhargrah.orders.events.OrderCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final OrdersRepository ordersRepository;

    public OrderEventHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent){
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderCreatedEvent,orderEntity);
        ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        OrderEntity order = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());

        if(null == order) return;

        order.setOrderStatus(orderApprovedEvent.getOrderStatus());
        ordersRepository.save(order);
    }
}
