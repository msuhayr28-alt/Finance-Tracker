package com.Suhayr.Finance.Tracker.service;

import com.Suhayr.Finance.Tracker.dto.TransactionDTO;
import com.Suhayr.Finance.Tracker.dto.TransactionRequest;
import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.model.Transactions;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.repository.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final CategoriesService categoriesService;

    public TransactionsService(TransactionsRepository transactionsRepository, CategoriesService categoriesService) {
        this.transactionsRepository = transactionsRepository;
        this.categoriesService = categoriesService;
    }

    // Helper to convert Entity -> DTO
    private TransactionDTO toDTO(Transactions transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getCategory().getName(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getTransactionDate()
        );
    }

    // Create
    public TransactionDTO addTransaction(TransactionRequest request, User user) {
        Categories category = categoriesService.findByName(request.getCategoryName())
                .orElseGet(() -> categoriesService.createCategory(request.getCategoryName()));

        Transactions transaction = new Transactions();
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());

        return toDTO(transactionsRepository.save(transaction));
    }

    // Get all for user
    public List<TransactionDTO> getUserTransactions(Long userId) {
        return transactionsRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Get single
    public Optional<TransactionDTO> getTransactionById(Long id, Long userId) {
        return transactionsRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .map(this::toDTO);
    }

    // Get by category
    public List<TransactionDTO> getUserTransactionByCategory(Long userId, Long categoryId) {
        return transactionsRepository.findByUserIdAndCategoryId(userId, categoryId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Update
    public TransactionDTO updateTransaction(Long id, TransactionRequest request, User user) {
        Transactions transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        Categories category = categoriesService.findByName(request.getCategoryName())
                .orElseGet(() -> categoriesService.createCategory(request.getCategoryName()));

        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());

        return toDTO(transactionsRepository.save(transaction));
    }

    // Delete
    public void deleteTransaction(Long id, User user) {
        Transactions transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        transactionsRepository.delete(transaction);
    }
}
