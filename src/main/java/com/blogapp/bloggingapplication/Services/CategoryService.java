package com.blogapp.bloggingapplication.Services;

import com.blogapp.bloggingapplication.payloads.CategoryDTO;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(CategoryDTO categoryDTO,String id)throws ExecutionException, InterruptedException;
    CategoryDTO getCategory(CategoryDTO categoryDTO,String id)throws ExecutionException, InterruptedException;
    String deleteCategory(String id)throws ExecutionException, InterruptedException;
    List<CategoryDTO> getAllCategories()throws ExecutionException, InterruptedException;
}
