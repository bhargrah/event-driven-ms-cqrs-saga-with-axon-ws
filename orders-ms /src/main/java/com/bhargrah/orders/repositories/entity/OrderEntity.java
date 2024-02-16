package com.bhargrah.orders.repositories.entity;

import com.bhargrah.orders.command.model.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="orders")
public class OrderEntity implements Serializable {


    @Id
    @Column(unique = true)
    public String orderId;

    private String productId;

    private String userId;

    private int quantity;

    private String addressId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
