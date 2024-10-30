package com.apps.trader.serivice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.trader.model.Asset;
import com.apps.trader.model.Coin;
import com.apps.trader.model.User;
import com.apps.trader.repository.AssetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    @Autowired
    private final AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) throws Exception {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());
        return assetRepository.save(asset);

    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {
        try {
            return assetRepository.findById(assetId).orElseThrow(() -> new Exception("Asset not found"));
        } catch (Exception e) {
            throw new Exception("Error fetching asset:\n", e);
        }
    }

    @Override
    public Asset getAssetByUserId(Long userId, Long assetId) throws Exception {
        return null;
    }

    @Override
    public List<Asset> getUserAssets(Long userId) throws Exception {
        try {
            return assetRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new Exception("Error fetching asset:\n", e);
        }
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {
        try {
            Asset oldAsset = assetRepository.findById(assetId).orElseThrow(() -> new Exception("Asset not found"));
            oldAsset.setQuantity(quantity);
            return assetRepository.save(oldAsset);

        } catch (Exception e) {
            throw new Exception("Error fetching asset:\n", e);
        }
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) throws Exception {
        try {
            return assetRepository.findByUserIdAndCoinId(userId, coinId);
        } catch (Exception e) {
            throw new Exception("Error fetching asset:\n", e);
        }
    }

    @Override
    public void deleteAsset(Long assetId) throws Exception {
        try {
            Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new Exception("Asset not found"));
            if (asset != null) {
                assetRepository.delete(asset);
            }

        } catch (Exception e) {
            throw new Exception("Error fetching asset:\n", e);
        }
    }

}
