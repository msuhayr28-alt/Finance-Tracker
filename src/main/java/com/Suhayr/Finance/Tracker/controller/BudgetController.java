package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.model.Budget;
import com.Suhayr.Finance.Tracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> setBudget(@RequestBody Budget budget){
        Budget savedBudget = budgetService.setBudget(budget);
        return ResponseEntity.ok(savedBudget);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Budget>> getUserBudgets(@PathVariable Long userId){
        List<Budget> budgets = budgetService.getUserBudget(userId);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<Budget> getBudgetForMonth(
            @PathVariable Long userId,
            @PathVariable Long categoryId,
            @RequestParam Integer month,
            @RequestParam Integer year){
        Optional<Budget> budget = budgetService.getBudgetForMonth(userId, categoryId, month, year);
        return budget.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody Budget budgetDetails){
        Budget updatedBudget = budgetService.updateBudget(id, budgetDetails);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id){
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
