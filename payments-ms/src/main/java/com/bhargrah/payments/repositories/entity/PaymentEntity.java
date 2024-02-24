package com.bhargrah.payments.repositories.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "payments")
public class PaymentEntity implements Serializable {

    private static final long serialVersionUID = 5059324003983825666L;
    @Column
    public String orderId;
    @Id
    private String paymentId;

}
