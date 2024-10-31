package com.apps.trader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apps.trader.model.Withdrawal;
import java.util.List;


@Repository
public interface WithdrawRepository extends JpaRepository<Withdrawal, Long> {

    List<Withdrawal> findByUserId(Long userId);
}
