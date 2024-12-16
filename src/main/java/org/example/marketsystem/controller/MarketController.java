package org.example.marketsystem.controller;

import org.example.marketsystem.model.MarketItem;
import org.example.marketsystem.model.Order;
import org.example.marketsystem.repo.UserRepository;
import org.example.marketsystem.service.MarketEngineService;
import org.example.marketsystem.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.marketsystem.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;
    private final MarketEngineService marketEngineService;
    private final UserRepository userRepository;

    @Autowired
    public MarketController(MarketService marketService, MarketEngineService marketEngineService, UserRepository userRepository) {
        this.marketService = marketService;
        this.marketEngineService = marketEngineService;
        this.userRepository = userRepository;
        marketEngineService.processOrdersConcurrently();
    }

    // Endpoint for users to place an order
    @PostMapping("/orders/{username}/add")
    public ResponseEntity<String> addOrder(@PathVariable String username, @RequestBody Order order) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found.");
        }

        if (order == null || order.getMarketItem() == null || order.getPrice() == null || order.getQuantity() == null) {
            return ResponseEntity.badRequest().body("Invalid order data.");
        }

        try {
            marketEngineService.createOrder(user, order.getPrice(), order.getQuantity(), order.getMarketItem().getId(), order.getOrderType());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Market item not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while adding the order.");
        }

        return ResponseEntity.ok("Order added successfully for user: " + username);
    }

    
    
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> findAllOrders() {
        List<Order> orders = marketService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/bids")
    public ResponseEntity<List<Order>> findBuyOrders() {
        List<Order> buyOrders = marketService.findOrdersByType("BUY");
        return ResponseEntity.ok(buyOrders);
    }

    @GetMapping("/orders/sells")
    public ResponseEntity<List<Order>> findSellOrders() {
        List<Order> sellOrders = marketService.findOrdersByType("SELL");
        return ResponseEntity.ok(sellOrders);
    }

    @PostMapping("/items/add")
    public ResponseEntity<String> addMarketItem(@RequestBody MarketItem marketItem) {
        if (marketItem == null || marketItem.getName() == null || marketItem.getDescription() == null) {
            return ResponseEntity.badRequest().body("Invalid market item data.");
        }

        marketService.createMarketItem(marketItem.getName(), marketItem.getDescription());
        return ResponseEntity.ok("Market item added successfully: " + marketItem.getName());
    }

    @GetMapping("/items")
    public ResponseEntity<List<MarketItem>> getMarketItems() {
        List<MarketItem> marketItems = marketService.findAllMarketItems();
        return ResponseEntity.ok(marketItems);
    }

    @PostMapping("/orders/match")
    public ResponseEntity<String> matchOrders() {
        try {
            marketEngineService.processOrdersConcurrently();
            return ResponseEntity.ok("Order matching started successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred while matching orders.");
        }
    }
}
