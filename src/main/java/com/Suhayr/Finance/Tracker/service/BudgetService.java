package com.Suhayr.Finance.Tracker.service;

import com.Suhayr.Finance.Tracker.dto.BudgetDTO;
import com.Suhayr.Finance.Tracker.model.Budget;
import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoriesService categoriesService;

    public BudgetService(BudgetRepository budgetRepository, CategoriesService categoriesService) {
        this.budgetRepository = budgetRepository;
        this.categoriesService = categoriesService;
    }

    private BudgetDTO toDTO(Budget budget) {
        return new BudgetDTO(
                budget.getId(),
                budget.getCategory().getName(),
                budget.getAmount(),
                budget.getMonth(),
                budget.getYear()
        );
    }

    public BudgetDTO createBudget(Budget budget) {
        return toDTO(budgetRepository.save(budget));
    }

    public List<BudgetDTO> getUserBudgets(Long userId) {
        return budgetRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<BudgetDTO> getBudgetForMonth(Long userId, Long categoryId, Integer month, Integer year) {
        return budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(userId, categoryId, month, year)
                .map(this::toDTO);
    }

    public BudgetDTO updateBudget(Long id, BudgetDTO budgetDTO, User user) {
        Budget existing = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        existing.setAmount(budgetDTO.getAmount());
        existing.setMonth(budgetDTO.getMonth());
        existing.setYear(budgetDTO.getYear());

        if (budgetDTO.getCategoryName() != null) {
            Categories category = categoriesService.findByName(budgetDTO.getCategoryName())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }

        return toDTO(budgetRepository.save(existing));
    }

    public void deleteBudget(Long id, User user) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        budgetRepository.deleteById(id);
    }
}
