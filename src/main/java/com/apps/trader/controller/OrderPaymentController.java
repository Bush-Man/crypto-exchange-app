package com.apps.trader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.serivice.PaymentOrderService;
import com.apps.trader.serivice.UserService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.apps.trader.enums.PaymentMethod;
import com.apps.trader.model.PaymentOrder;
import com.apps.trader.model.User;
import com.apps.trader.response.PaymentResponse;

@AllArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class OrderPaymentController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final PaymentOrderService paymentOrderService;

    @PostMapping("/{paymentMethod}/amout/{amount}")
    public ResponseEntity<?> paymentHandler(@PathVariable PaymentMethod paymentMethodReq, @PathVariable Long amount, @RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserByJwt(jwt);
            PaymentResponse paymentResponse;

            PaymentOrder paymentOrder = paymentOrderService.createOrderPayment(user, amount, paymentMethodReq);

            if (paymentMethodReq.equals(PaymentMethod.RAZORPAY)) {
                paymentResponse = paymentOrderService.raziorOrderPayment(user, amount, paymentOrder.getId());

            } else {
                paymentResponse = paymentOrderService.stripeOrderPayment(user, amount, paymentOrder.getId());
            }
            return ResponseEntity.ok(paymentResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);

        }

    }
}
