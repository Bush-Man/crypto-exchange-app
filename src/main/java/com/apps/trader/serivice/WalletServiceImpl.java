package com.apps.trader.serivice;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.trader.enums.OrderType;
import com.apps.trader.model.Order;
import com.apps.trader.model.User;
import com.apps.trader.model.Wallet;
import com.apps.trader.repository.WalletRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    @Autowired
    private final WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(Long userId) throws Exception {
        try {

            Wallet wallet = walletRepository.findByUserId(userId);
            if (wallet == null) {
                throw new Exception("Wallet Does Not Exist");
            }

            return wallet;

        } catch (Exception e) {
            throw new Exception("Error Fetching Wallet:" + e.getMessage(), e);
        }

    }

    @Override
    public Wallet deposit(Wallet wallet, Long amount) throws Exception {
        try {
            Wallet existingWallet = walletRepository.findById(wallet.getId()).orElseThrow(() -> new Exception("Wallet not found"));;

            BigDecimal balance = existingWallet.getBalance().add(BigDecimal.valueOf(amount));
            wallet.setBalance(balance);
            return walletRepository.save(existingWallet);
        } catch (Exception e) {

            throw new Exception("Error depositing amount: " + e.getMessage(), e);

        }

    }

    @Override
    public Wallet findById(Long walletId) throws Exception {
        try {
            Wallet existingWallet = walletRepository.findById(walletId)
                    .orElseThrow(() -> new Exception("Wallet not found"));
            return existingWallet;
        } catch (Exception e) {

            throw new Exception("Error wallet not found:" + e.getMessage(), e);

        }

    }

    @Override
    public Wallet walletToWalletTransaction(User sender, Wallet receiver, Long amount) throws Exception {
        try {
            Wallet existingSenderWallet = walletRepository.findByUserId(sender.getId());

            Wallet existingReceiverWallet = walletRepository.findById(receiver.getId()).get();

            if (existingReceiverWallet == null || existingSenderWallet == null) {

                throw new Exception("Wallet not found");
            }
            BigDecimal senderBalance = existingSenderWallet.getBalance();
            if (senderBalance.compareTo(BigDecimal.valueOf(amount)) < 0) {

                throw new Exception("Insufficient Balance");

            }
            BigDecimal senderNewBalance = existingSenderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
            existingSenderWallet.setBalance(senderNewBalance);
            Wallet updatedSenderWallet = walletRepository.save(existingSenderWallet);

            BigDecimal receiverNewBalance = existingReceiverWallet.getBalance().add(BigDecimal.valueOf(amount));
            existingReceiverWallet.setBalance(receiverNewBalance);
            walletRepository.save(existingReceiverWallet);

            return updatedSenderWallet;
        } catch (Exception e) {

            throw new Exception("Error depositing amount: " + e.getMessage(), e);

        }

    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        try {

            Wallet wallet = walletRepository.findByUserId(user.getId());
            BigDecimal walletBalance = wallet.getBalance();
            BigDecimal orderPrice = order.getPrice();
            if (walletBalance.compareTo(orderPrice) < 0) {
                throw new Exception("Insufficient Balance");
            }

            if (order.getOrderType().equals(OrderType.BUY)) {
                walletBalance.subtract(orderPrice);

                wallet.setBalance(walletBalance);
                walletRepository.save(wallet);
            }
            if (order.getOrderType().equals(OrderType.SELL)) {
                walletBalance.add(orderPrice);

                wallet.setBalance(walletBalance);
                walletRepository.save(wallet);
            }
            return wallet;

        } catch (Exception e) {
            throw new Exception("Order Payment Error:" + e.getMessage(), e);
        }

    }
}
