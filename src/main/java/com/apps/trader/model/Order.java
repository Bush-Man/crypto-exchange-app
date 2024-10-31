package com.apps.trader.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.apps.trader.enums.OrderStatus;
import com.apps.trader.enums.OrderType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable=false)
    private BigDecimal price;

    private LocalDateTime timeStamp= LocalDateTime.now();

    @OneToOne(mappedBy="order",cascade=CascadeType.ALL)
    private OrderItem orderItem;
}

