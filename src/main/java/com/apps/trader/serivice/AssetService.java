package com.apps.trader.serivice;

import java.util.List;

import com.apps.trader.model.Asset;
import com.apps.trader.model.Coin;
import com.apps.trader.model.User;

public interface AssetService {

    Asset createAsset(User user, Coin coin, double quantity) throws Exception;

    Asset getAssetById(Long assetId) throws Exception;

    Asset getAssetByUserId(Long userId, Long assetId) throws Exception;

    List<Asset> getUserAssets(Long userId) throws Exception;

    Asset updateAsset(Long assetId, double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId, String coinId) throws Exception;

    void deleteAsset(Long assetId) throws Exception;

}
