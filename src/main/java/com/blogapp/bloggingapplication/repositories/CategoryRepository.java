package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
