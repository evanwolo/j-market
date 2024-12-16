package org.example.marketsystem.model;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class MarketOrderQueues {

    private PriorityBlockingQueue<Order> buyOrders;
    private PriorityBlockingQueue<Order> sellOrders;

    // Constructor accepting buy and sell order lists
    public MarketOrderQueues(List<Order> buyOrders, List<Order> sellOrders) {
        // Initialize the buyOrders as a max-heap (highest price first)
        this.buyOrders = new PriorityBlockingQueue<>(buyOrders.size(), (o1, o2) ->
                Double.compare(o2.getPrice(), o1.getPrice()));  // Comparator for descending order (max-heap)

        // Initialize the sellOrders as a min-heap (lowest price first)
        this.sellOrders = new PriorityBlockingQueue<>(sellOrders.size(), (o1, o2) ->
                Double.compare(o1.getPrice(), o2.getPrice()));  // Comparator for ascending order (min-heap)

        // Add all orders to the respective priority queues
        this.buyOrders.addAll(buyOrders);
        this.sellOrders.addAll(sellOrders);
    }

    // Methods to check if the queues are empty
    public boolean isBuyQueueEmpty() {
        return buyOrders.isEmpty();
    }

    public boolean isSellQueueEmpty() {
        return sellOrders.isEmpty();
    }

    // Methods to peek at the highest priority order in the queues
    public Order peekBuyOrder() {
        return buyOrders.peek();
    }

    public Order peekSellOrder() {
        return sellOrders.peek();
    }

    // Methods to remove the highest priority order from the queues
    public void pollBuyOrder() {
        buyOrders.poll();
    }

    public void pollSellOrder() {
        sellOrders.poll();
    }

    // Optionally, methods for taking orders in a blocking way
    public Order takeBuyOrder() throws InterruptedException {
        return buyOrders.take();  // This will block if the queue is empty
    }

    public Order takeSellOrder() throws InterruptedException {
        return sellOrders.take();  // This will block if the queue is empty
    }

    public void removeBuyOrder() {
        buyOrders.poll();
    }

    public void removeSellOrder() {
        sellOrders.poll();
    }
}
