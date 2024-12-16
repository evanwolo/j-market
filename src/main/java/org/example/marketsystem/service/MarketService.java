package org.example.marketsystem.service;

import org.example.marketsystem.model.MarketItem;
import org.example.marketsystem.model.Order;
import org.example.marketsystem.repo.MarketItemRepository;
import org.example.marketsystem.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {

    private final OrderRepository orderRepository;
    private final MarketItemRepository marketItemRepository;

    @Autowired
    public MarketService(OrderRepository orderRepository, MarketItemRepository marketItemRepository) {
        this.orderRepository = orderRepository;
        this.marketItemRepository = marketItemRepository;
    }

    public void createMarketItem(String name, String description) {
        MarketItem marketItem = new MarketItem(name,description);

        marketItemRepository.save(marketItem);
    }

    public List<MarketItem> findAllMarketItems() {
        List<MarketItem> marketItems = marketItemRepository.findAll();
        return marketItems;
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> findOrdersByType(String orderType) {
        return orderRepository.findAllByOrderType(orderType);
    }
}
