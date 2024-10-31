package com.apps.trader.serivice;

import com.apps.trader.model.PaymentDetails;
import com.apps.trader.model.User;

public interface PaymentDetailsService {

    PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String bankName, String ifsec, User user);

    PaymentDetails getUserPaymentDetails(User user);
}
