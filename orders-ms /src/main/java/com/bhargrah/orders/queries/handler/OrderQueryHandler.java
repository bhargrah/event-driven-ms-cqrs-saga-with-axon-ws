package com.bhargrah.orders.queries.handler;


import com.bhargrah.orders.queries.model.OrderSummary;
import com.bhargrah.orders.repositories.OrdersRepository;
import com.bhargrah.orders.repositories.entity.OrderEntity;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderQueryHandler {

    OrdersRepository ordersRepository;

    @Autowired
    public OrderQueryHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");
    }


}
