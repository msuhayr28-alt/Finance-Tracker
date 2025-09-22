package com.Suhayr.Finance.Tracker.service;

import com.Suhayr.Finance.Tracker.model.Transactions;
import com.Suhayr.Finance.Tracker.repository.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<Transactions> getTransactionById(Long id){
        return transactionsRepository.findById(id);
    }
    public List<Transactions> getUserTransactionByCategory(Long userId, Long categoryId){
        return transactionsRepository.findByUserIdAndCategoryId(userId, categoryId);
    }
    public Transactions updateTransaction(Long id,Transactions transactionDetails){
        Transactions transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(transactionDetails.getAmount());
        transaction.setDescription(transactionDetails.getDescription());
        transaction.setTransactionDate(transactionDetails.getTransactionDate());
        transaction.setCategory(transactionDetails.getCategory());

        return transactionsRepository.save(transaction);
    }

    public void deleteTransaction(Long id){
        transactionsRepository.deleteById(id);
    }
}
