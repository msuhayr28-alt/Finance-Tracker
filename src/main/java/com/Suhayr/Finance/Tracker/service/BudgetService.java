package com.Suhayr.Finance.Tracker.service;

import com.Suhayr.Finance.Tracker.model.Budget;
import com.Suhayr.Finance.Tracker.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget setBudget(Budget budget){
        return budgetRepository.save(budget);
    }

    public List<Budget> getUserBudget(Long userId){
        return budgetRepository.findByUserId(userId);
    }

    public Optional<Budget> getBudgetForMonth(Long userId, Long categoryId, Integer month,Integer year){
        return budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(userId, categoryId, month, year);
    }

    public Budget updateBudget(Long id, Budget budgetDetails){
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budget.setAmount(budgetDetails.getAmount());
        budget.setMonth(budgetDetails.getMonth());
        budget.setYear(budgetDetails.getYear());
        budget.setCategory(budgetDetails.getCategory());

        return budgetRepository.save(budget);
    }

    public void deleteBudget(Long id){
        if(!budgetRepository.existsById(id)){
            throw new RuntimeException("Budget not found");
        }
        budgetRepository.deleteById(id);
    }
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

}
