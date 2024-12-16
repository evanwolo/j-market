package org.example.marketsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "market_item_stats")
public class MarketItemStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "market_item_id", referencedColumnName = "id")
    private MarketItem marketItem;

    private Integer totalOpenBuys = 0;
    private Integer totalOpenSells = 0;
    private Integer totalFulfilledBuys = 0;
    private Integer totalFulfilledSells = 0;
    private Double marketCap = 0.0;
    private Double totalPriceTraded = 0.0;
    private Long totalQuantityTraded = 0L;
    private Double averagePricePerUnit = 0.0;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public MarketItem getMarketItem() {
        return marketItem;
    }

    public void setMarketItem(MarketItem marketItem) {
        this.marketItem = marketItem;
    }

    public Integer getTotalOpenBuys() {
        return totalOpenBuys;
    }

    public void setTotalOpenBuys(Integer totalOpenBuys) {
        this.totalOpenBuys = totalOpenBuys;
    }

    public Integer getTotalOpenSells() {
        return totalOpenSells;
    }

    public void setTotalOpenSells(Integer totalOpenSells) {
        this.totalOpenSells = totalOpenSells;
    }

    public Integer getTotalFulfilledBuys() {
        return totalFulfilledBuys;
    }

    public void setTotalFulfilledBuys(Integer totalFulfilledBuys) {
        this.totalFulfilledBuys = totalFulfilledBuys;
    }

    public Integer getTotalFulfilledSells() {
        return totalFulfilledSells;
    }

    public void setTotalFulfilledSells(Integer totalFulfilledSells) {
        this.totalFulfilledSells = totalFulfilledSells;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public Double getTotalPriceTraded() {
        return totalPriceTraded;
    }

    public void setTotalPriceTraded(Double totalPriceTraded) {
        this.totalPriceTraded = totalPriceTraded;
    }

    public Integer getTotalQuantityTraded() {
        return Math.toIntExact(totalQuantityTraded);
    }

    public void setTotalQuantityTraded(Integer totalQuantityTraded) {
        this.totalQuantityTraded = Long.valueOf(totalQuantityTraded);
    }

    public Double getAveragePricePerUnit() {
        return averagePricePerUnit;
    }

    public void setAveragePricePerUnit(Double averagePricePerUnit) {
        this.averagePricePerUnit = averagePricePerUnit;
    }

    // Helper methods
    public synchronized void updateForBuyOrder(int quantity, double price) {
        totalOpenBuys += quantity;
        marketCap += (quantity * price);
        totalPriceTraded += (quantity * price);
        totalQuantityTraded += quantity;
        calculateAveragePricePerUnit();

        // Debugging log
        System.out.println("Updated stats for buy order: Total Open Buys = " + totalOpenBuys +
                ", Market Cap = " + marketCap + ", Total Price Traded = " + totalPriceTraded +
                ", Total Quantity Traded = " + totalQuantityTraded + ", Avg Price = " + averagePricePerUnit);
    }

    public synchronized void updateForSellOrder(int quantity, double price) {
        totalOpenSells += quantity;
        marketCap += (quantity * price);
        totalPriceTraded += (quantity * price);
        totalQuantityTraded += quantity;
        calculateAveragePricePerUnit();

        // Debugging log
        System.out.println("Updated stats for sell order: Total Open Sells = " + totalOpenSells +
                ", Market Cap = " + marketCap + ", Total Price Traded = " + totalPriceTraded +
                ", Total Quantity Traded = " + totalQuantityTraded + ", Avg Price = " + averagePricePerUnit);
    }

    public synchronized void updateForFulfilledTransaction(int buyQuantity, int sellQuantity, double price) {
        totalFulfilledBuys += buyQuantity;
        totalFulfilledSells += sellQuantity;
        totalOpenBuys -= buyQuantity;
        totalOpenSells -= sellQuantity;
        marketCap -= (buyQuantity * price);
        totalPriceTraded += (buyQuantity * price);
        totalQuantityTraded += buyQuantity;
        calculateAveragePricePerUnit();

        // Debugging log
        System.out.println("Updated stats for fulfilled transaction: Total Fulfilled Buys = " + totalFulfilledBuys +
                ", Total Fulfilled Sells = " + totalFulfilledSells + ", Market Cap = " + marketCap +
                ", Total Price Traded = " + totalPriceTraded + ", Total Quantity Traded = " + totalQuantityTraded +
                ", Avg Price = " + averagePricePerUnit);
    }

    private void calculateAveragePricePerUnit() {
        if (totalQuantityTraded > 0) {
            averagePricePerUnit = totalPriceTraded / totalQuantityTraded;
        }
    }
}
