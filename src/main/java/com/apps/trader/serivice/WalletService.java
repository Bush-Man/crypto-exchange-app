package com.apps.trader.serivice;

import com.apps.trader.model.Order;
import com.apps.trader.model.User;
import com.apps.trader.model.Wallet;

public interface WalletService {
    Wallet getUserWallet(Long userId) throws Exception;

    Wallet deposit(Wallet wallet, Long amount) throws Exception;

    Wallet findById(Long id) throws Exception;
    
    Wallet walletToWalletTransaction(User sender, Wallet receiver, Long amount) throws Exception;
    
    Wallet payOrderPayment(Order order, User user) throws Exception;

}
