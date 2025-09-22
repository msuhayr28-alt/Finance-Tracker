package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.model.Budget;
import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.service.BudgetService;
import com.Suhayr.Finance.Tracker.service.CategoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoriesService categoriesService;

    public BudgetController(BudgetService budgetService, CategoriesService categoriesService) {
        this.budgetService = budgetService;
        this.categoriesService = categoriesService;
    }


    @PostMapping
    public ResponseEntity<Budget> setBudget(@RequestParam String categoryName,
                                            @RequestParam Double amount,
                                            @RequestParam Integer month,
                                            @RequestParam Integer year){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Categories categories = categoriesService.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(categories);
        budget.setAmount(amount);
        budget.setMonth(month);
        budget.setYear(year);
        return ResponseEntity.ok(budgetService.setBudget(budget));
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getUserBudgets(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(budgetService.getUserBudget(user.getId()));
    }

    @GetMapping("category/{categoryName}")
    public ResponseEntity<Budget> getBudgetForMonth(
            @PathVariable String categoryName,
            @RequestParam Integer month,
            @RequestParam Integer year){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Categories categories = categoriesService.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Optional<Budget> budget = budgetService.getBudgetForMonth(user.getId(), categories.getId(), month, year);
        return budget.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody Budget budgetDetails){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Budget existingBudget = budgetService.getBudgetById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if(!existingBudget.getUser().getId().equals(user.getId())){
            return ResponseEntity.status(403).build();
        }
        existingBudget.setAmount(budgetDetails.getAmount());
        existingBudget.setMonth(budgetDetails.getMonth());
        existingBudget.setYear(budgetDetails.getYear());

        if(budgetDetails.getCategory() != null){
            Categories categories = categoriesService.findByName(budgetDetails.getCategory().getName())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingBudget.setCategory(categories);
        }
        Budget updatedBudget = budgetService.updateBudget(id, existingBudget);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Budget budget = budgetService.getBudgetById(id)
                        .orElseThrow(() -> new RuntimeException("Budget not found"));
        if(!budget.getUser().getId().equals(user.getId())){
            return ResponseEntity.status(403).build();
        }
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
