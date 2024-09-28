package com.blogapp.bloggingapplication.Services.impl;

import com.blogapp.bloggingapplication.Services.CategoryService;
import com.blogapp.bloggingapplication.Services.FirebaseService;
import com.blogapp.bloggingapplication.entities.Category;
import com.blogapp.bloggingapplication.exceptions.ResourceNotFoundException;
import com.blogapp.bloggingapplication.payloads.CategoryDTO;
import com.blogapp.bloggingapplication.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    FirebaseService firebaseService;
    private static Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = this.DTOtoCategory(categoryDTO);
        try {
            String id = category.getCategoryTitle();
            String updateTime =  firebaseService.saveDocument("categories", id, category);
            System.out.println("Category saved successfully with update time: " + updateTime);
        } catch (
                ExecutionException |
                InterruptedException e) {
            logger.error("Error saving category to document " + e);
        }
        return this.CategorytoDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, String id) throws ExecutionException, InterruptedException{
        Map<String, Object> updates = new HashMap<>();
        if (categoryDTO.getCategoryTitle() != null) {
            updates.put("categoryTitle",categoryDTO.getCategoryTitle());
        }
        if (categoryDTO.getCategoryDescription() != null) {
            updates.put("categoryDescription", categoryDTO.getCategoryDescription());
        }
        firebaseService.updateDocument("categories",id, updates);

        // Fetch the updated document
        CategoryDTO updatedCategory = firebaseService.getDocument("categories", id, CategoryDTO.class);
        return updatedCategory;
    }

    @Override
    public CategoryDTO getCategory(CategoryDTO categoryDTO, String id)throws ExecutionException, InterruptedException {
        Category category=firebaseService.getDocument("categories",id,Category.class);
        if (category == null) {
            throw new ResourceNotFoundException("Category with id: " + id + " not found");
        }
        return CategorytoDTO(category);
    }

    @Override
    public String deleteCategory(String id) throws ExecutionException, InterruptedException{
        return (firebaseService.deleteDocument("categories",id));

    }

    @Override
    public List<CategoryDTO> getAllCategories() throws ExecutionException, InterruptedException{
        List<Category> categories=this.firebaseService.getAllDocument("categories",Category.class);
        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> this.CategorytoDTO(category)).collect(Collectors.toList());
        return categoryDTOS;
    }

    CategoryDTO CategorytoDTO(Category category) {
        CategoryDTO categoryDTO = this.modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    Category DTOtoCategory(CategoryDTO categoryDTO) {
        Category category = this.modelMapper.map(categoryDTO, Category.class);
        return category;
    }
}
