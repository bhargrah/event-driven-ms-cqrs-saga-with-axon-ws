package com.bhargrah.orders.events;

import com.bhargrah.orders.command.model.OrderStatus;
import lombok.Data;
import lombok.Value;

@Value
public class OrderApprovedEvent {

    private final String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
