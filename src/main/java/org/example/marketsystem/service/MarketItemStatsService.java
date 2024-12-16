package org.example.marketsystem.service;

import org.example.marketsystem.model.MarketItem;
import org.example.marketsystem.model.MarketItemStats;
import org.example.marketsystem.repo.MarketItemStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketItemStatsService {

    private final MarketItemStatsRepository marketItemStatsRepository;

    @Autowired
    public MarketItemStatsService(MarketItemStatsRepository marketItemStatsRepository) {
        this.marketItemStatsRepository = marketItemStatsRepository;
    }

    public void updateStatsForBuyOrder(MarketItem marketItem, int quantity, double price) {
        MarketItemStats stats = getOrCreateStatsForMarketItem(marketItem);
        stats.updateForBuyOrder(quantity, price);
        marketItemStatsRepository.save(stats);
        marketItemStatsRepository.flush();
    }

    public void updateStatsForSellOrder(MarketItem marketItemId, int quantity, double price) {
        MarketItemStats stats = getOrCreateStatsForMarketItem(marketItemId);
        stats.updateForSellOrder(quantity, price);
        marketItemStatsRepository.save(stats);
        marketItemStatsRepository.flush();
    }

    public void updateStatsForFulfilledTransaction(MarketItem marketItemId, int buyQuantity, int sellQuantity, double price) {
        MarketItemStats stats = getOrCreateStatsForMarketItem(marketItemId);
        stats.updateForFulfilledTransaction(buyQuantity, sellQuantity, price);
        marketItemStatsRepository.save(stats);
        marketItemStatsRepository.flush();
    }

    private MarketItemStats getOrCreateStatsForMarketItem(MarketItem marketItem) {
        MarketItemStats stats = marketItemStatsRepository.findByMarketItemId(marketItem.getId());
        if (stats == null) {
            stats = new MarketItemStats();
            stats.setMarketItem(marketItem);
            marketItemStatsRepository.save(stats);
        }
        return stats;
    }
}
