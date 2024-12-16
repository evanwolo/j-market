package org.example.marketsystem.repo;

import org.example.marketsystem.model.MarketItem;
import org.example.marketsystem.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByMarketItem_IdAndOrderTypeOrderByPriceDesc(Long marketItem_id, String orderType, Pageable pageable);
    List<Order> findAllByOrderType(String orderType);

    @Query("SELECT AVG(o.price) FROM Order o WHERE o.marketItem.id = :marketItemId AND o.fulfilled = true AND o.orderType = 'BUY' ORDER BY o.createdAt DESC")
    Double findAverageOfLast10BuyOrders(@Param("marketItemId") Long marketItemId);

    @Query("SELECT o FROM Order o WHERE o.fulfilled = false")
    List<Order> findAllUnfulfilledOrders();

    @Query("SELECT o FROM Order o WHERE o.marketItem = :marketItem AND o.orderType = 'BUY' AND o.fulfilled = false ORDER BY o.price DESC")
    List<Order> findBuyOrdersByMarketItem(@Param("marketItem") MarketItem marketItem);

    @Query("SELECT o FROM Order o WHERE o.marketItem = :marketItem AND o.orderType = 'SELL' AND o.fulfilled = false ORDER BY o.price ASC")
    List<Order> findSellOrdersByMarketItem(@Param("marketItem") MarketItem marketItem);



}
