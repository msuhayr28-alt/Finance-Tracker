package com.Suhayr.Finance.Tracker.service;

import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public Categories addCategory(Categories category){
        return categoriesRepository.save(category);
    }
    public Categories createCategory(String name) {
        Categories category = new Categories();
        category.setName(name);
        return categoriesRepository.save(category);
    }


    public List<Categories> getAllCategories(){
        return categoriesRepository.findAll();
    }

    public Optional<Categories> findByName(String name){
        return categoriesRepository.findByName(name);
    }

    public Categories updateCategory(Long id, Categories categoryDetails){
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categories.setName(categoryDetails.getName());
        return categoriesRepository.save(categories);
    }

    public void deleteCategory(Long id){
        categoriesRepository.deleteById(id);
    }
}
