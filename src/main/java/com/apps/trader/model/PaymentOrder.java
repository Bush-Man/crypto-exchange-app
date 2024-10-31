package com.apps.trader.model;

import com.apps.trader.enums.OrderPaymentStatus;
import com.apps.trader.enums.PaymentMethod;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderPaymentStatus orderPaymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    private User user;

    private Long amount;

}
