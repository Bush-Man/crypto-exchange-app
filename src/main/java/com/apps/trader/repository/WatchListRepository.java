package com.apps.trader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apps.trader.model.WatchList;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {

    WatchList findByUserId(Long userId);

}
