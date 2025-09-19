package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.model.Transactions;
import com.Suhayr.Finance.Tracker.service.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping
    public ResponseEntity<Transactions> addTransaction(@RequestBody Transactions transaction){
        Transactions savedTransaction = transactionsService.addTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transactions>> getUserTransaction(@PathVariable Long userId){
        List<Transactions> transactions = transactionsService.getUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<Transactions>> getUserTransactionByCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId){
        List<Transactions> transactions = transactionsService.getUserTransactionByCategory(userId, categoryId);
        return ResponseEntity.ok(transactions);

    }
    @PutMapping("/{id}")
    public ResponseEntity<Transactions> updateTransaction(@PathVariable Long id, @RequestBody Transactions transaction){
        transaction.setId(id);
        Transactions updatedTransaction = transactionsService.updateTransaction(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        transactionsService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
