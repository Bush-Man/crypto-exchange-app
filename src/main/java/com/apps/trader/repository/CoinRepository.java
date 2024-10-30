package com.apps.trader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apps.trader.model.Coin;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {

}
