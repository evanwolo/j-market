package org.example.marketsystem.service;

import org.example.marketsystem.model.MarketItem;
import org.example.marketsystem.model.MarketItemStats;
import org.example.marketsystem.model.Order;
import org.example.marketsystem.model.Transaction;
import org.example.marketsystem.repo.MarketItemStatsRepository;
import org.example.marketsystem.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void createTransaction(Order buyOrder, Order sellOrder, float tradePrice, int tradeQuantity) {
        // Create a transaction
        Transaction transaction = new Transaction();
        transaction.setBuyOrder(buyOrder);
        transaction.setSellOrder(sellOrder);
        transaction.setPrice(tradePrice);
        transaction.setQuantity(tradeQuantity);

        // Save transaction to DB
        transactionRepository.save(transaction);
    }
}
