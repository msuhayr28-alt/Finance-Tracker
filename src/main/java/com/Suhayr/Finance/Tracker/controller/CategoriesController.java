package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.model.Categories;
import com.Suhayr.Finance.Tracker.responses.ApiResponse;
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
    public ResponseEntity<ApiResponse<Categories>> createCategory(@RequestBody Categories categories){
        Categories savedCategory = categoriesService.addCategory(categories);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category added successfully",savedCategory));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Categories>>> getAllCategories(){
        List<Categories> categories = categoriesService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse<>(true, "Categories retrieved successfully", categories));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Categories>> getCategoryByName(@RequestParam String name){
        return categoriesService.findByName(name)
                .map(categories -> ResponseEntity.ok(new ApiResponse<>(true, "Categrory retrieved successfully", categories)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Category not found", null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categories>> updateCategory(@PathVariable Long id, @RequestBody Categories categoryDetails){
        Categories updatedCategory = categoriesService.updateCategory(id, categoryDetails);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category updated successfully", updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id){
        categoriesService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted successfully", null));
    }


}
