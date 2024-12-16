package org.example.marketsystem.repo;
import org.example.marketsystem.model.MarketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketItemRepository extends JpaRepository<MarketItem, String> {
    Optional<MarketItem> findByName(String name);
    Optional<MarketItem> findById(Long id);
}