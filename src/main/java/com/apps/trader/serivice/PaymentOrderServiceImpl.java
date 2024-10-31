package com.apps.trader.serivice;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.apps.trader.enums.OrderPaymentStatus;
import com.apps.trader.enums.PaymentMethod;
import com.apps.trader.model.PaymentOrder;
import com.apps.trader.model.User;
import com.apps.trader.repository.PaymentOrderRepository;
import com.apps.trader.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {

    @Autowired
    private final PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private final String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private final String razorPayApiKey;

    @Value("${razorpay.api.secret}")
    private final String razorPaySecretKey;

    @Override
    public PaymentOrder createOrderPayment(User user, Long amount, PaymentMethod PaymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentMethod(PaymentMethod);
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setOrderPaymentStatus(OrderPaymentStatus.PENDING);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getOrderPaymentById(Long id) {
        return paymentOrderRepository.findById(id).get();
    }

    @Override
    public Boolean proceedOrderPayment(PaymentOrder paymentOrder, String paymentId) throws Exception {

        if (paymentOrder.getOrderPaymentStatus() == null) {
            paymentOrder.setOrderPaymentStatus(OrderPaymentStatus.PENDING);
        }
        try {

            if (paymentOrder.getOrderPaymentStatus().equals(OrderPaymentStatus.PENDING)) {
                if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {

                    RazorpayClient razorPay = new RazorpayClient(razorPayApiKey, razorPaySecretKey);
                    Payment payment = razorPay.payments.fetch(paymentId);
                   // Integer amount = payment.get("amount");
                    String status = payment.get("status");

                    if (status.equals("captured")) {
                        paymentOrder.setOrderPaymentStatus(OrderPaymentStatus.SUCCESS);
                        return true;
                    }
                    paymentOrder.setOrderPaymentStatus(OrderPaymentStatus.FAILED);
                    paymentOrderRepository.save(paymentOrder);
                    return false;

                }
                paymentOrder.setOrderPaymentStatus(OrderPaymentStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;

            }
            return false;

        } catch (RazorpayException e) {
            throw new Exception("Error: Order Payment", e);
        }

    }

    @Override
    public PaymentResponse raziorOrderPayment(User user, Long amount, Long orderId) throws Exception {
        amount = amount * 100;

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorPayApiKey, razorPaySecretKey);
            JSONObject paymentLinkReq = new JSONObject();
            paymentLinkReq.put("amount", amount);
            paymentLinkReq.put("currency", "USD");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkReq.put("customer", customer);

            JSONObject notify = new JSONObject();
            customer.put("email", true);
            paymentLinkReq.put("notify", notify);

            paymentLinkReq.put("remainder_enabled", true);

            paymentLinkReq.put("callback_url", "http://localhost:8080/api/wallet?order_id=" + orderId);
            paymentLinkReq.put("callback_method", "get");

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkReq);
            String paymentLinkUrl = paymentLink.get("short_url");
           // String paymentLinkId = paymentLink.get("id");

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setPaymentUrl(paymentLinkUrl);

            return paymentResponse;
        } catch (RazorpayException e) {
            throw new Exception("Razor Payment Error:", e);
        }

    }

    @Override
    public PaymentResponse stripeOrderPayment(User user, Long amount, Long orderId) throws Exception {

        try {
            Stripe.apiKey = stripeSecretKey;

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/api/wallet?order_id=" + orderId)
                    .setCancelUrl("http://localhost:8080/api/payment/cancel")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmount(amount * 100)
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                            .builder()
                                            .setName("Top Up Wallet")
                                            .build()
                                    ).build()
                            ).build()
                    ).build();

            Session session = Session.create(params);
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setPaymentUrl(session.getUrl());
            return paymentResponse;
        } catch (StripeException e) {
            throw new Exception("Stripe payment error:", e);

        }
    }

}
