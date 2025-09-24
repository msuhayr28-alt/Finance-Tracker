package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.dto.TransactionDTO;
import com.Suhayr.Finance.Tracker.dto.TransactionRequest;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.service.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionsService transactionService;

    public TransactionsController(TransactionsService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(transactionService.addTransaction(request, user));
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getUserTransactions() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(transactionService.getUserTransactions(user.getId()));
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCategory(
            @PathVariable Long categoryId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<TransactionDTO> transactions = transactionService.getUserTransactionByCategory(user.getId(), categoryId);

        return ResponseEntity.ok(transactions);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionService.getTransactionById(id, user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(transactionService.updateTransaction(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transactionService.deleteTransaction(id, user);
        return ResponseEntity.noContent().build();
    }
}

