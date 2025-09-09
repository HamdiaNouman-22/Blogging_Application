package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.Services.CategoryService;
import com.blogapp.bloggingapplication.payloads.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Validated
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Operation(summary = "Create a new category", description = "Adds a new category to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory(@Validated @RequestBody CategoryDTO categoryDTO){
        CategoryDTO category=this.categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @Operation(summary = "Update a category", description = "Updates an existing category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/{categoryid}")
    public ResponseEntity<CategoryDTO> updateCategory(@Validated CategoryDTO categoryDTO,@PathVariable("categoryid") String categoryid)throws ExecutionException, InterruptedException{
        CategoryDTO category=this.categoryService.updateCategory(categoryDTO, categoryid);
        return ResponseEntity.ok(category);
    }
    @Operation(summary = "Get all categories", description = "Fetches a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories fetched successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    })
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAllCategories()throws ExecutionException, InterruptedException{
        return ResponseEntity.ok(this.categoryService.getAllCategories());
    }
    @Operation(summary = "Delete a category", description = "Deletes a category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{categoryid}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryid") String categoryid)throws ExecutionException, InterruptedException{
        this.categoryService.deleteCategory(categoryid);
        return new ResponseEntity<>(Map.of("message","Category deleted sucessfully"),HttpStatus.OK);
    }
}
