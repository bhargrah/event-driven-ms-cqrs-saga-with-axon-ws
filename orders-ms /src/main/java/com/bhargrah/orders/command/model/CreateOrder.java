package com.bhargrah.orders.command.model;

import lombok.Data;

@Data
public class CreateOrder {

    private String productId;
    private Integer quantity;
    private String addressId;

}
