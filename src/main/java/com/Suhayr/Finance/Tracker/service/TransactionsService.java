package com.Suhayr.Finance.Tracker.service;

import com.Suhayr.Finance.Tracker.model.Transactions;
import com.Suhayr.Finance.Tracker.repository.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;

    public TransactionsService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public Transactions addTransaction(Transactions transaction){
        return transactionsRepository.save(transaction);
    }

    public List<Transactions> getUserTransactions(Long userId){
        return transactionsRepository.findByUserId(userId);
    }
    public List<Transactions> getUserTransactionByCategory(Long userId, Long categoryId){
        return transactionsRepository.findByUserIdAndCategoryId(userId, categoryId);
    }
    public Transactions updateTransaction(Transactions transaction){
        return transactionsRepository.save(transaction);
    }

    public void deleteTransaction(Long id){
        transactionsRepository.deleteById(id);
    }
}
