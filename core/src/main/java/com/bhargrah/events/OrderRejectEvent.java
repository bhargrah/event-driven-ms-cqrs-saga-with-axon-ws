package com.bhargrah.events;

import com.bhargrah.model.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectEvent {

    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;

}
