package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.dto.TransactionDTO;
import com.Suhayr.Finance.Tracker.dto.TransactionRequest;
import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.responses.ApiResponse;
import com.Suhayr.Finance.Tracker.service.CategoriesService;
import com.Suhayr.Finance.Tracker.service.TransactionsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionsService transactionService;
    private final CategoriesService categoriesService;

    public TransactionsController(TransactionsService transactionService, CategoriesService categoriesService) {
        this.transactionService = transactionService;
        this.categoriesService = categoriesService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> createTransaction(@Valid  @RequestBody TransactionRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TransactionDTO created = transactionService.addTransaction(request, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transactions created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getUserTransactions() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TransactionDTO> fetchedTransaction = transactionService.getUserTransactions(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction retrieved successfully", fetchedTransaction));
    }
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByCategory(
            @PathVariable String categoryName) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Categories category = categoriesService.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        List<TransactionDTO> transactions = transactionService.getUserTransactionByCategory(user.getId(), category.getId());

        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction retrieved successfully", transactions));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransactionById(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionService.getTransactionById(id, user.getId())
                .map(transaction -> ResponseEntity.ok(new ApiResponse<>(true, "Transaction retrieved successfully", transaction)))
                .orElse(ResponseEntity.status(404)
                        .body(new ApiResponse<>(false, "Transaction not found with ID" + id, null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TransactionDTO updated = transactionService.updateTransaction(id, request, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transactionService.deleteTransaction(id, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction deleted successfully", null));
    }
}

