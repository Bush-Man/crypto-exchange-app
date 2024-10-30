package com.apps.trader.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.serivice.CoinService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.apps.trader.model.Coin;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/coins")
@AllArgsConstructor
public class CoinController {

    @Autowired
    private final CoinService coinService;

    @Autowired
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Coin>> getCoins(@RequestParam("page") int page) {
        try {
            List<Coin> coins = coinService.getCoinList(page);
            return ResponseEntity.ok(coins);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode> getMarketChart(
            @PathVariable String coinId,
            @RequestParam("days") int days) {
        try {
            String chart = coinService.getMarketChart(coinId, days);
            JsonNode jsonNode = objectMapper.readTree(chart);

            return ResponseEntity.ok(jsonNode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) {
        try {
            String coin = coinService.searchCoin(keyword);
            JsonNode jsonNode = objectMapper.readTree(coin);

            return ResponseEntity.ok(jsonNode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50CoinsByMarketCapRank() {
        try {
            String coin = coinService.getTop50CoinsByMarketCapRank();
            JsonNode jsonNode = objectMapper.readTree(coin);

            return ResponseEntity.ok(jsonNode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/trending")
    public ResponseEntity<JsonNode> getTrendingCoins() {
        try {
            String coin = coinService.getTrendingCoins();
            JsonNode jsonNode = objectMapper.readTree(coin);

            return ResponseEntity.ok(jsonNode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/details/{coinId}")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) {
        try {
            String coin = coinService.getCoinDetails(coinId);
            JsonNode jsonNode = objectMapper.readTree(coin);
            return ResponseEntity.ok(jsonNode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
