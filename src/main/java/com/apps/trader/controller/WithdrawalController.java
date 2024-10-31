package com.apps.trader.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.serivice.UserService;
import com.apps.trader.serivice.WalletService;
import com.apps.trader.serivice.WithdrawService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.apps.trader.enums.WalletTransactionType;
import com.apps.trader.model.User;
import com.apps.trader.model.Wallet;
import com.apps.trader.model.WalletTransaction;
import com.apps.trader.model.Withdrawal;

@AllArgsConstructor
@RestController
@RequestMapping("/api/withdrawal")
public class WithdrawalController {

    @Autowired
    private final WithdrawService withdrawService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final WalletService walletService;

    @PostMapping("/{amount}")
    public ResponseEntity<?> withdrawalRequest(@PathVariable Long amount, @RequestHeader("Authorization") String jwt) {

        try {
            User user = userService.findUserByJwt(jwt);
            Wallet wallet = walletService.getUserWallet(user.getId());

            Withdrawal withdrawal = withdrawService.requestWithdrawal(amount, user);
            walletService.deposit(wallet, -amount);
            WalletTransaction walletTransaction = walletTransactionService.createTransaction(
                    wallet,
                    WalletTransactionType.WITHDRAW,
                    null,
                    amount
            );
            return new ResponseEntity<>(withdrawal, HttpStatus.OK);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }

    }

    @PatchMapping("/admin/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(@PathVariable Long id, @PathVariable boolean accept, @RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserByJwt(jwt);
            Wallet wallet = walletService.getUserWallet(user.getId());
            Withdrawal withdrawal = withdrawService.proceedWithdrawal(id, accept);
            if (!accept) {
                walletService.deposit(wallet, withdrawal.getAmount());
            }
            return new ResponseEntity<>(withdrawal, HttpStatus.OK);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);

        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getWithdrawalHistory(@RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserByJwt(jwt);
            List<Withdrawal> withdrawalHistory = withdrawService.getUserWithdrawalHistory(user);

            return new ResponseEntity<>(withdrawalHistory, HttpStatus.OK);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);

        }
    }

    @GetMapping("/admin/withdrawal")
    public ResponseEntity<?> getAllWithdrawals(@RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserByJwt(jwt);
            List<Withdrawal> withdrawalRequests = withdrawService.getAllWithdrawalRequest();

            return new ResponseEntity<>(withdrawalRequests, HttpStatus.OK);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);

        }
    }
}
