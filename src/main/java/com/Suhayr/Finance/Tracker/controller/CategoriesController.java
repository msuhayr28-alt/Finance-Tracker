package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.service.CategoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @PostMapping
    public ResponseEntity<Categories> createCategory(@RequestBody Categories categories){
        Categories savedCategory = categoriesService.addCategory(categories);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<Categories>> getAllCategories(){
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @GetMapping("/search")
    public ResponseEntity<Categories> getCategoryByName(@RequestParam String name){
        return categoriesService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categories> updateCategory(@PathVariable Long id, @RequestBody Categories categoryDetails){
        return ResponseEntity.ok(categoriesService.updateCategory(id, categoryDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoriesService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


}
