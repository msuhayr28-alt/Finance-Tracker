package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.dto.BudgetDTO;
import com.Suhayr.Finance.Tracker.dto.BudgetRequest;
import com.Suhayr.Finance.Tracker.model.Budget;
import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.service.BudgetService;
import com.Suhayr.Finance.Tracker.service.CategoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Categories category = categoriesService.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category);
        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        return ResponseEntity.ok(budgetService.createBudget(budget));
    }

    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getBudgets() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(budgetService.getUserBudgets(user.getId()));
    }

    @GetMapping("category/{categoryName}")
    public ResponseEntity<BudgetDTO> getBudgetForMonth(
            @PathVariable String categoryName,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Categories category = categoriesService.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return budgetService.getBudgetForMonth(user.getId(), category.getId(), month, year)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(@PathVariable Long id, @RequestBody BudgetDTO budgetDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(budgetService.updateBudget(id, budgetDTO, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        budgetService.deleteBudget(id, user);
        return ResponseEntity.noContent().build();
    }
}
