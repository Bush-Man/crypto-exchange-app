package com.apps.trader.serivice;

import com.apps.trader.enums.PaymentMethod;
import com.apps.trader.model.PaymentOrder;
import com.apps.trader.model.User;
import com.apps.trader.response.PaymentResponse;

public interface PaymentOrderService {

    PaymentOrder createOrderPayment(User user, Long amount, PaymentMethod PaymentMethod);

    PaymentOrder getOrderPaymentById(Long id);

    Boolean proceedOrderPayment(PaymentOrder paymentOrder, String paymentId) throws Exception;

    PaymentResponse raziorOrderPayment(User user, Long amount, Long orderId) throws Exception;

    PaymentResponse stripeOrderPayment(User user, Long amount, Long orderId) throws Exception;

}
