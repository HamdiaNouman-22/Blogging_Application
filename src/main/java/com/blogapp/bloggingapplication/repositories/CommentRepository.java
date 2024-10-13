package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
