package com.apps.trader.requests;

import com.apps.trader.enums.OrderType;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private String coinId;
    private double quantity;
    private OrderType orderType;
}
