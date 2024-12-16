package org.example.marketsystem.repo;

import org.example.marketsystem.model.MarketItemStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketItemStatsRepository extends JpaRepository<MarketItemStats, Long> {
    MarketItemStats findByMarketItemId(Long marketItemId);
}
