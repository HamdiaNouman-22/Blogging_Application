package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.Category;
import com.blogapp.bloggingapplication.entities.Post;
import com.blogapp.bloggingapplication.entities.User;
import com.blogapp.bloggingapplication.payloads.PostDTO;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.google.cloud.spring.data.firestore.repository.config.FirestoreRepositoryConfigurationExtension;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends FirestoreReactiveRepository<Post> {
}
