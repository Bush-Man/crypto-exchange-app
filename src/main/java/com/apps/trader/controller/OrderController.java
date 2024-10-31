package com.apps.trader.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.model.Coin;
import com.apps.trader.model.Order;
import com.apps.trader.model.User;
import com.apps.trader.requests.CreateOrderRequest;
import com.apps.trader.serivice.CoinService;
import com.apps.trader.serivice.OrderService;
import com.apps.trader.serivice.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.apps.trader.enums.OrderType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final CoinService coinService;

    // @Autowired
    // private WalletTransactionService walletTransactionService;
    @PutMapping("/pay/{orderId}/user/{userId}")
    public ResponseEntity<?> payOrderPayment(@RequestHeader("Authorization") String jwt, @RequestBody CreateOrderRequest orderRequest) {
        try {
            User user = userService.findUserByJwt(jwt);
            Coin coin = coinService.findById(orderRequest.getCoinId());

            Order order = orderService.processOrder(coin, orderRequest.getQuantity(), orderRequest.getOrderType(), user);

            return ResponseEntity.ok(order);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Wallet not found.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transaction Error: " + e.getMessage());
        }

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) {
        try {
            if (jwt == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Missing");
            }
            User user = userService.findUserByJwt(jwt);
            Order order = orderService.getOrderById(orderId);

            if (order.getUser().getId().equals(user.getId())) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not Authorized");

            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Order not found.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order Query Error: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllOrdersForUser(@RequestParam(required = false) OrderType order_type, @RequestParam(required = false) String asset_symbol, @RequestHeader("Authorization") String jwt) {
        try {
            if (jwt == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Missing");
            }
            Long userId = userService.findUserByJwt(jwt).getId();
            List<Order> orders = orderService.getAllUserOrders(userId, order_type, asset_symbol);
            return ResponseEntity.ok(orders);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Order not found.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order Query Error: " + e.getMessage());
        }

    }

}
