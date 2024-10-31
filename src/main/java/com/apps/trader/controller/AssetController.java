package com.apps.trader.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.model.Asset;
import com.apps.trader.serivice.AssetService;
import com.apps.trader.serivice.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/asset")
@AllArgsConstructor
public class AssetController {

    @Autowired
    private final AssetService assetService;
    @Autowired
    private final UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<?> getAssetById(@PathVariable Long assetId) {
        try {
            Asset asset = assetService.getAssetById(assetId);
            return ResponseEntity.ok(asset);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<?> getAssetByuserIdAndCoinId(@PathVariable String coinId,@RequestHeader("Authorization") String jwt) {
        try {
            Long userId = userService.findUserByJwt(jwt).getId();
            Asset asset = assetService.findAssetByUserIdAndCoinId(userId, coinId);
            return ResponseEntity.ok(asset);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAssetForUser(@RequestHeader("Authorization") String jwt) {
        try {
            Long userId = userService.findUserByJwt(jwt).getId();
            List<Asset>assets = assetService.getUserAssets(userId);
            return ResponseEntity.ok(assets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

}
