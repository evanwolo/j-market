package org.example.marketsystem.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "buy_order_id")
    private Order buyOrder;

    @ManyToOne
    @JoinColumn(name = "sell_order_id")
    private Order sellOrder;

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    private float tradePrice;
    private int quantity;

    public Transaction() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Order getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(Order SellOrder) {
        this.sellOrder = SellOrder;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(Order BuyOrder) {
        this.buyOrder = BuyOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public float getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(float tradePrice) {
        this.tradePrice = tradePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", buyOrder=" + buyOrder +
                ", sellOrder=" + sellOrder +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public void setPrice(float tradePrice) {
        this.tradePrice = tradePrice;
    }
}
