package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.Services.CategoryService;
import com.blogapp.bloggingapplication.payloads.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory(@Validated @RequestBody CategoryDTO categoryDTO){
        CategoryDTO category=this.categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @PostMapping("/{categoryid}")
    public ResponseEntity<CategoryDTO> updateCategory(@Validated CategoryDTO categoryDTO,@PathVariable("categoryid") String categoryid)throws ExecutionException, InterruptedException{
        CategoryDTO category=this.categoryService.updateCategory(categoryDTO, categoryid);
        return ResponseEntity.ok(category);
    }
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAllCategories()throws ExecutionException, InterruptedException{
        return ResponseEntity.ok(this.categoryService.getAllCategories());
    }

    @DeleteMapping("/{categoryid}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryid") String categoryid)throws ExecutionException, InterruptedException{
        this.categoryService.deleteCategory(categoryid);
        return new ResponseEntity<>(Map.of("message","Category deleted sucessfully"),HttpStatus.OK);
    }
}
