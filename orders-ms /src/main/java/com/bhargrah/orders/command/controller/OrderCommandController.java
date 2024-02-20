package com.bhargrah.orders.command.controller;

import com.bhargrah.model.OrderStatus;
import com.bhargrah.orders.command.CreateOrderCommand;
import com.bhargrah.orders.command.model.CreateOrder;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    @Autowired
    public CommandGateway commandGateway;

    public OrderCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@RequestBody CreateOrder createOrder){

        var createOrderCommand = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                        .productId(createOrder.getProductId())
                                .addressId(createOrder.getAddressId())
                                        .quantity(createOrder.getQuantity())
                                            .orderStatus(OrderStatus.CREATED)
                                                    .userId("bhargrah")
                                                        .build();

        String returnValue = commandGateway.sendAndWait(createOrderCommand);

        return returnValue;
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }



}
