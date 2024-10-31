package com.apps.trader.serivice;

import java.util.List;

import com.apps.trader.model.User;
import com.apps.trader.model.Withdrawal;

public interface WithdrawService {

    Withdrawal requestWithdrawal(Long amount, User user);

    Withdrawal proceedWithdrawal(Long withdrawalId, boolean accept) throws Exception;

    List<Withdrawal> getUserWithdrawalHistory(User user);

    List<Withdrawal> getAllWithdrawalRequest();

}
