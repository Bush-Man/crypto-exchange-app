package com.apps.trader.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.serivice.WalletService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.apps.trader.model.Wallet;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.apps.trader.model.Coin;
import com.apps.trader.model.Order;
import com.apps.trader.model.User;
import com.apps.trader.model.WalletTransaction;
import com.apps.trader.repository.UserRepository;
import com.apps.trader.requests.CreateOrderRequest;
import com.apps.trader.serivice.CoinService;
import com.apps.trader.serivice.OrderService;
import com.apps.trader.serivice.UserService;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/wallet")
@AllArgsConstructor
public class WalletController {

    @Autowired
    private final WalletService walletService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final CoinService coinService;

    @Autowired
    private final OrderService orderService;

    /*
     * TODO
     * Use userId from JWT Token
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserWallet(@PathVariable Long userId) {
        try {
            Wallet wallet = walletService.findById(userId);
            return ResponseEntity.ok(wallet);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Wallet not found.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error depositing amount: " + e.getMessage());
        }
    }

    @PutMapping("/transfer/{userId}/{walletId}")
    public ResponseEntity<?> walletToWalletTransfer(@PathVariable Long userId, @PathVariable Long walletId, @RequestBody WalletTransaction transactionReq) {
        try {
            User user = userRepository.findById(userId).get();
            Wallet receiver = walletService.findById(walletId);
            Wallet sender_wallet = walletService.walletToWalletTransaction(user, receiver, transactionReq.getAmount());

            return ResponseEntity.ok(sender_wallet);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Wallet not found.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transaction Error: " + e.getMessage());
        }

    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<?> payOrderWallet(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserByJwt(jwt);
            Order order = orderService.getOrderById(orderId);
            Wallet wallet = walletService.payOrderPayment(order, user);

            return ResponseEntity.ok(wallet);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Wallet not found.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transaction Error: " + e.getMessage());
        }

    }

}
