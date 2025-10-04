package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.dto.BudgetDTO;
import com.Suhayr.Finance.Tracker.dto.BudgetRequest;
import com.Suhayr.Finance.Tracker.model.Budget;
import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.responses.ApiResponse;
import com.Suhayr.Finance.Tracker.service.BudgetService;
import com.Suhayr.Finance.Tracker.service.CategoriesService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<BudgetDTO>> setBudget(@Valid  @RequestBody BudgetRequest budgetRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Budget budget = new Budget();
        budget.setUser(user);

        Categories category = new Categories();
        category.setName(budgetRequest.getCategoryName());
        budget.setCategory(category);

        budget.setAmount(budgetRequest.getAmount());
        budget.setMonth(budgetRequest.getMonth());
        budget.setYear(budgetRequest.getYear());

        BudgetDTO created = budgetService.createBudget(budget);
        return ResponseEntity.ok(new ApiResponse<>(true, "Budget created successfully", created));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<BudgetDTO>>> getBudgets() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<BudgetDTO> budgets = budgetService.getUserBudgets(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Budget retrieved successfully", budgets));
    }

    @GetMapping("category/{categoryName}")
    public ResponseEntity<ApiResponse<BudgetDTO>> getBudgetForMonth(
            @PathVariable String categoryName,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Categories category = categoriesService.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return budgetService.getBudgetForMonth(user.getId(), category.getId(), month, year)
                .map(budget -> ResponseEntity.ok(new ApiResponse<>(true, "Budget retrieved successfully", budget)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Budget not found", null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BudgetDTO>> updateBudget(@PathVariable Long id, @RequestBody BudgetDTO budgetDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BudgetDTO updated = budgetService.updateBudget(id, budgetDTO, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Budget updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBudget(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        budgetService.deleteBudget(id, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Budget deleted successfully", null));
    }
}
