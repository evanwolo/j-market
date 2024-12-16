package org.example.marketsystem.service;

import org.example.marketsystem.model.*;
import org.example.marketsystem.repo.MarketItemStatsRepository;
import org.example.marketsystem.repo.OrderRepository;
import org.example.marketsystem.repo.MarketItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;

@Service
public class MarketEngineService {

    private final OrderRepository orderRepository;
    private final MarketItemRepository marketItemRepository;
    private final MarketItemStatsRepository marketItemStatsRepository;
    private final MarketItemStatsService marketItemStatsService;
    private final TransactionService transactionService;

    @Autowired
    public MarketEngineService(OrderRepository orderRepository,
                               MarketItemRepository marketItemRepository,
                               MarketItemStatsRepository marketItemStatsRepository,
                               MarketItemStatsService marketItemStatsService,
                               TransactionService transactionService) {
        this.orderRepository = orderRepository;
        this.marketItemRepository = marketItemRepository;
        this.marketItemStatsRepository = marketItemStatsRepository;
        this.marketItemStatsService = marketItemStatsService;
        this.transactionService = transactionService;
    }
    ExecutorService executorService;
    @Transactional
    public void processOrdersConcurrently() {
        executorService  = Executors.newFixedThreadPool(10);

        List<MarketItem> marketItems = marketItemRepository.findAll();
        for (MarketItem marketItem : marketItems) {
            executorService.submit(() -> {
                processOrdersForMarketItem(marketItem);
            });
        }
    }
    
    public void shutdown(){
        executorService.shutdown();
    }

    public void createOrder(User user, double price, int quantity, Long marketItemId, String orderType) {
        MarketItem marketItem = marketItemRepository.findById(marketItemId)
                .orElseThrow(() -> new IllegalArgumentException("Market item not found"));

        // Create the new order and associate it with the user
        Order order = new Order();
        order.setUser(user);  // Set the user placing the order
        order.setMarketItem(marketItem);
        order.setPrice((float) price);
        order.setQuantity(quantity);
        order.setOrderType(orderType);

        orderRepository.save(order);

        // Update market item stats based on the order type
        MarketItemStats stats = marketItemStatsRepository.findByMarketItemId(marketItemId);
        if (stats != null) {
            if ("BUY".equals(orderType)) {
                stats.updateForBuyOrder(quantity, price);
            } else if ("SELL".equals(orderType)) {
                stats.updateForSellOrder(quantity, price);
            }
            marketItemStatsRepository.save(stats);
        }
    }

    @Transactional
    protected void processOrdersForMarketItem(MarketItem marketItem) {
        MarketOrderQueues queues = new MarketOrderQueues(
                orderRepository.findBuyOrdersByMarketItem(marketItem),
                orderRepository.findSellOrdersByMarketItem(marketItem)
        );

        OrderMatcher orderMatcher = new OrderMatcher();
        orderMatcher.matchOrders(queues);
    }

    // Order matcher class to match buy and sell orders
    public class OrderMatcher {

        public void matchOrders(MarketOrderQueues queues) {
            while (true) {
                synchronized (queues) {
                    if (queues.isBuyQueueEmpty() || queues.isSellQueueEmpty()) {
                        break;  // Exit loop if any queue is empty
                    }

                    Order buyOrder = queues.peekBuyOrder();
                    Order sellOrder = queues.peekSellOrder();

                    if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                        float tradePrice = sellOrder.getPrice();
                        int tradeQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());

                        transactionService.createTransaction(buyOrder, sellOrder, tradePrice, tradeQuantity);
                        marketItemStatsService.updateStatsForBuyOrder(buyOrder.getMarketItem(), tradeQuantity, tradePrice);
                        marketItemStatsService.updateStatsForSellOrder(sellOrder.getMarketItem(), tradeQuantity, tradePrice);

                        // After trade, update orders
                        buyOrder.setQuantity(buyOrder.getQuantity() - tradeQuantity);
                        sellOrder.setQuantity(sellOrder.getQuantity() - tradeQuantity);

                        if (buyOrder.getQuantity() == 0) {
                            queues.removeBuyOrder();
                        }
                        if (sellOrder.getQuantity() == 0) {
                            queues.removeSellOrder();
                        }
                    } else {
                        break;  // Exit loop if prices don't match
                    }
                }
            }
        }
    }
}
