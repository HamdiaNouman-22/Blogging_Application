package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.Category;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

public interface CategoryRepository extends FirestoreReactiveRepository<Category> {
}
