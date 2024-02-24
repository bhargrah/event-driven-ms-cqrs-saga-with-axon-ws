package com.bhargrah.orders.queries.model;

import com.bhargrah.model.OrderStatus;
import lombok.Value;

@Value
public class OrderSummary {

    private final String orderId;
    private final OrderStatus orderStatus;
    private final String message;

}
