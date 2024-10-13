package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.Category;
import com.blogapp.bloggingapplication.entities.Post;
import com.blogapp.bloggingapplication.entities.User;
import com.blogapp.bloggingapplication.payloads.PostDTO;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
public Page<Post> findByCategory(Category category,Pageable paged);
public Page<Post> findByUser(User user, Pageable paged);
public  List<Post> findByTitleContaining(String keywords);
}
