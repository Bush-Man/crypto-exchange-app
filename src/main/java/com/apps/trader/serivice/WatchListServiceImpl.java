package com.apps.trader.serivice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.trader.model.Coin;
import com.apps.trader.model.User;
import com.apps.trader.model.WatchList;
import com.apps.trader.repository.WatchListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private final WatchListRepository watchListRepository;

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return watchListRepository.save(watchList);
    }

    @Override
    public WatchList findUserWatchList(Long userId) {
        return watchListRepository.findByUserId(userId);
    }

    @Override
    public WatchList findById(Long watchListId) throws Exception {
        return watchListRepository.findById(watchListId).orElseThrow(() -> new Exception("WatchList Not Found"));
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) throws Exception {
        WatchList watchList = watchListRepository.findByUserId(user.getId());
        if (watchList == null) {
            throw new Exception("WatchList Not Found");
        }

        if (watchList.getCoins().contains(coin)) {
            watchList.getCoins().remove(coin);
        } else {
            watchList.getCoins().add(coin);

        }
        watchListRepository.save(watchList);

        return coin;
    }

}
