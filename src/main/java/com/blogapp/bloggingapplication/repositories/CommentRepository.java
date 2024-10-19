package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.Comment;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

public interface CommentRepository extends FirestoreReactiveRepository<Comment> {
}
