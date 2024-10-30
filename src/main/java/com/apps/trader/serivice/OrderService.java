package com.apps.trader.serivice;

import java.util.List;

import com.apps.trader.enums.OrderType;
import com.apps.trader.model.Coin;
import com.apps.trader.model.Order;
import com.apps.trader.model.OrderItem;
import com.apps.trader.model.User;


public interface  OrderService {

    public Order getOrderById(Long id)throws Exception;

    public Order createOrder(User user, OrderItem orderItem,OrderType orderType)throws Exception;

    List<Order> getAllUserOrders(Long userId,OrderType orderType,String assetSymbol) throws Exception;

    Order processOrder(Coin coin, double quantity,OrderType orderType,User user) throws Exception;

}
