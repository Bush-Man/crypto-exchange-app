package com.apps.trader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apps.trader.model.OrderItem;

@Repository
public interface OrderItemRepository extends  JpaRepository<OrderItem, Long> {

}
