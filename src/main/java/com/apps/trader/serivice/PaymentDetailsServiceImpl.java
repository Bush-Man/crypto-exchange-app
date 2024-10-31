package com.apps.trader.serivice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.trader.model.PaymentDetails;
import com.apps.trader.model.User;
import com.apps.trader.repository.PaymentDetailsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    @Autowired
    private final PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String bankName,
            String ifsec, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);
        paymentDetails.setIfsec(ifsec);
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }

}
