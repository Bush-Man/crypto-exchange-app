package com.apps.trader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apps.trader.model.PaymentOrder;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

}
