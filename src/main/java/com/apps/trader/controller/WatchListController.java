package com.apps.trader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.model.Coin;
import com.apps.trader.model.User;
import com.apps.trader.model.WatchList;
import com.apps.trader.serivice.CoinService;
import com.apps.trader.serivice.UserService;
import com.apps.trader.serivice.WatchListService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    private final WatchListService watchListService;

    @Autowired
    private final CoinService coinService;

    @Autowired
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserWatchList(@RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserByJwt(jwt);
            WatchList watchList = watchListService.findUserWatchList(user.getId());
            return ResponseEntity.ok(watchList);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<?> createWatchList(@RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserByJwt(jwt);
            WatchList watchList = watchListService.createWatchList(user);
            return ResponseEntity.ok(watchList);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWatchList(@PathVariable Long id) {
        try {

            WatchList watchList = watchListService.findById(id);
            return ResponseEntity.ok(watchList);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

    @PatchMapping("/add/coin{coinId}")
    public ResponseEntity<?> addCoinToWatchList(@PathVariable String coinId, @RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserByJwt(jwt);
            Coin coin = coinService.findById(coinId);
            Coin addedCoin = watchListService.addItemToWatchList(coin, user);
            return ResponseEntity.ok(addedCoin);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

}
