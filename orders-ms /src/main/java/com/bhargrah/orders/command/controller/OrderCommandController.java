package com.bhargrah.orders.command.controller;

import com.bhargrah.model.OrderStatus;
import com.bhargrah.orders.command.CreateOrderCommand;
import com.bhargrah.orders.command.model.CreateOrder;
import com.bhargrah.orders.queries.handler.FindOrderQuery;
import com.bhargrah.orders.queries.model.OrderSummary;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    @Autowired
    public CommandGateway commandGateway;

    @Autowired
    public QueryGateway queryGateway;

    public OrderCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public OrderSummary createOrder(@RequestBody CreateOrder createOrder) {

        String orderId = UUID.randomUUID().toString();

        var createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId)
                .productId(createOrder.getProductId())
                .addressId(createOrder.getAddressId())
                .quantity(createOrder.getQuantity())
                .orderStatus(OrderStatus.CREATED)
                .userId("bhargrah")
                .build();

        SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = queryGateway.subscriptionQuery(new FindOrderQuery(orderId),
                ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class));

        try {
            commandGateway.sendAndWait(createOrderCommand);
            //String returnValue = commandGateway.sendAndWait(createOrderCommand);
            return queryResult.updates().blockFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            queryResult.close();
        }
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }


}
