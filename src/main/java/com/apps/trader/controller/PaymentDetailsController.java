package com.apps.trader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.model.PaymentDetails;
import com.apps.trader.serivice.PaymentDetailsService;
import com.apps.trader.serivice.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.apps.trader.model.User;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentDetailsController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final PaymentDetailsService paymentDetailsService;

    @PostMapping("/payment-details")
    public ResponseEntity<?> addPaymentDetails(@RequestHeader("Authorization") String jwt, @RequestBody PaymentDetails paymentDetailsReq) {
        try {

            User user = userService.findUserByJwt(jwt);

            PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                    paymentDetailsReq.getAccountHolderName(),
                    paymentDetailsReq.getAccountHolderName(),
                    paymentDetailsReq.getBankName(),
                    paymentDetailsReq.getIfsec(),
                    user
            );

            return ResponseEntity.ok(paymentDetails);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

    @GetMapping("/payment-details")
    public ResponseEntity<?> getPaymentDetails(@RequestHeader("Authorization") String jwt) {
        try {

            User user = userService.findUserByJwt(jwt);

            PaymentDetails paymentDetails = paymentDetailsService.getUserPaymentDetails(user);

            return ResponseEntity.ok(paymentDetails);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

}
