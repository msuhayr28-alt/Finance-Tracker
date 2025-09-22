package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.model.Transactions;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.service.CategoriesService;
import com.Suhayr.Finance.Tracker.service.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;
    private final CategoriesService categoriesService;

    public TransactionsController(TransactionsService transactionsService, CategoriesService categoriesService) {
        this.transactionsService = transactionsService;
        this.categoriesService = categoriesService;
    }

    @PostMapping
    public ResponseEntity<Transactions> addTransaction(
            @RequestParam String categoryName,
            @RequestParam Double amount,
            @RequestParam(required = false) String description){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Categories categories = categoriesService.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transactions transactions = new Transactions();
        transactions.setUser(user);
        transactions.setCategory(categories);
        transactions.setAmount(amount);
        transactions.setDescription(description);
        transactions.setTransactionDate(LocalDateTime.now());

        return ResponseEntity.ok(transactionsService.addTransaction(transactions));
    }
    @GetMapping
    public ResponseEntity<List<Transactions>> getUserTransaction(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(transactionsService.getUserTransactions(user.getId()));
    }
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Transactions>> getUserTransactionByCategory(
            @PathVariable String categoryName){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Categories categories = categoriesService.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Transactions> transactions = transactionsService.getUserTransactionByCategory(user.getId(), categories.getId());
        return ResponseEntity.ok(transactions);

    }
    @PutMapping("/{id}")
    public ResponseEntity<Transactions> updateTransaction(@PathVariable Long id, @RequestBody Transactions transactionDetails){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Transactions existingTransaction = transactionsService.getTransactionById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!existingTransaction.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }
        existingTransaction.setAmount(transactionDetails.getAmount());
        existingTransaction.setDescription(transactionDetails.getDescription());
        existingTransaction.setTransactionDate(transactionDetails.getTransactionDate() != null ?
                transactionDetails.getTransactionDate() : LocalDateTime.now());

        if(transactionDetails.getCategory() != null){
            Categories categories = categoriesService.findByName(transactionDetails.getCategory().getName())
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));
            existingTransaction.setCategory(categories);

        }


        Transactions updatedTransaction = transactionsService.updateTransaction(id, existingTransaction);
        return ResponseEntity.ok(updatedTransaction);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Transactions transaction = transactionsService.getTransactionById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }
        transactionsService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
