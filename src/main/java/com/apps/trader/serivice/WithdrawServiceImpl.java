package com.apps.trader.serivice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.trader.enums.WithdrawStatus;
import com.apps.trader.model.User;
import com.apps.trader.model.Withdrawal;
import com.apps.trader.repository.WithdrawRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private final WithdrawRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setWithdrawStatus(WithdrawStatus.PENDING);

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal proceedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Withdrawal pendingWithdrawal = withdrawalRepository.findById(withdrawalId).orElseThrow(() -> new Exception("Withdrawal not found"));
        if (accept) {

            pendingWithdrawal.setWithdrawStatus(WithdrawStatus.SUCCESS);
            pendingWithdrawal.setDate(LocalDateTime.now());
        } else {
            pendingWithdrawal.setWithdrawStatus(WithdrawStatus.DECLINE);
            pendingWithdrawal.setDate(LocalDateTime.now());

        }
        return withdrawalRepository.save(pendingWithdrawal);

    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }

}
