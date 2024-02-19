package com.bhargrah.orders.command;

import com.bhargrah.orders.command.model.OrderStatus;
import com.bhargrah.orders.events.OrderApprovedEvent;
import com.bhargrah.orders.events.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import javax.persistence.criteria.Order;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    public OrderAggregate() { }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent){
          this.orderId = orderCreatedEvent.getOrderId();
          this.productId = orderCreatedEvent.getProductId();
          this.userId = orderCreatedEvent.getUserId();
          this.quantity = orderCreatedEvent.getQuantity();
          this.addressId = orderCreatedEvent.getAddressId();
          this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand){
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @EventSourcingHandler
    protected void on(OrderApprovedEvent orderApprovedEvent){
        this.orderStatus = orderApprovedEvent.getOrderStatus();
    }
}
