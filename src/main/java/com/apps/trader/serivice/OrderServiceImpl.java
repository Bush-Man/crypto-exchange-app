package com.apps.trader.serivice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.trader.enums.OrderStatus;
import com.apps.trader.enums.OrderType;
import com.apps.trader.model.Asset;
import com.apps.trader.model.Coin;
import com.apps.trader.model.Order;
import com.apps.trader.model.OrderItem;
import com.apps.trader.model.User;
import com.apps.trader.repository.OrderItemRepository;
import com.apps.trader.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

    @Autowired
    private final WalletService walletService;

    @Autowired
    private final AssetService assetService;

    @Override
    public Order getOrderById(Long id) throws Exception {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new Exception("Order not found"));
            return order;

        } catch (Exception e) {
            throw new Exception("Error Fetching Order:", e);
        }
    }

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) throws Exception {
        try {
            double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
            Order order = new Order();
            order.setUser(user);
            order.setOrderItem(orderItem);
            order.setOrderType(orderType);
            order.setTimeStamp(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.PENDING);
            order.setPrice(BigDecimal.valueOf(price));
            Order savedOrder = orderRepository.save(order);
            return savedOrder;

        } catch (Exception e) {
            throw new Exception("Error creating order:" + e.getMessage(), e);

        }
    }

    @Override
    public List<Order> getAllUserOrders(Long userId, OrderType orderType, String assetSymbol) throws Exception {
        try {

            List<Order> orders = orderRepository.findByUserId(userId);
            return orders;
        } catch (Exception e) {
            throw new Exception("Error creating order:" + e.getMessage(), e);

        }
    }

    private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setQuantity(quantity);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    private Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Order quantity should be greater than 0");

        }
        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order, user);
        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);

        // create asset
        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(), order.getOrderItem().getCoin().getId());
        if (oldAsset == null) {
            assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());

        } else {
            assetService.updateAsset(oldAsset.getId(), quantity);
        }
        return savedOrder;

    }

    @Transactional
    private Order sellAsset(Coin coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Order quantity should be greater than 0");

        }
        double sellPrice = coin.getCurrentPrice();
        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
        double buyPrice = assetToSell.getBuyPrice();

        if (assetToSell.getId() != null) {
            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
            Order order = createOrder(user, orderItem, OrderType.SELL);
            orderItem.setOrder(order);
            if (assetToSell.getQuantity() >= quantity) {
                order.setOrderStatus(OrderStatus.SUCCESS);
                order.setOrderType(OrderType.SELL);
                Order savedOrder = orderRepository.save(order);
                walletService.payOrderPayment(order, user);

                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {

                    assetService.deleteAsset(updatedAsset.getId());

                }
                return savedOrder;

            }
            throw new Exception("Insufficient Quantity To Sell");
        }
        throw new Exception("Asset not found");

    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {

        switch (orderType) {
            case BUY -> {
                return buyAsset(coin, quantity, user);
            }
            case SELL -> {
                return sellAsset(coin, quantity, user);
            }
            default ->
                throw new Exception("Invalid Order Type");
        }
    }

}
