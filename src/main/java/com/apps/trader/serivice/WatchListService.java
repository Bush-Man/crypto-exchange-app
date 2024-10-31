package com.apps.trader.serivice;

import com.apps.trader.model.Coin;
import com.apps.trader.model.User;
import com.apps.trader.model.WatchList;

public interface WatchListService {

    WatchList createWatchList(User user);

    WatchList findUserWatchList(Long userId);

    WatchList findById(Long watchListId) throws Exception;

    Coin addItemToWatchList(Coin coin, User user) throws Exception;

}
