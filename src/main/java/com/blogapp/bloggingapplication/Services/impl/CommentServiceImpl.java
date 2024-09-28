package com.blogapp.bloggingapplication.Services.impl;

import com.blogapp.bloggingapplication.Services.CommentService;
import com.blogapp.bloggingapplication.Services.FirebaseService;
import com.blogapp.bloggingapplication.entities.Comment;
import com.blogapp.bloggingapplication.entities.Post;
import com.blogapp.bloggingapplication.entities.User;
import com.blogapp.bloggingapplication.exceptions.ResourceNotFoundException;
import com.blogapp.bloggingapplication.payloads.CommentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    FirebaseService firebaseService;

    @Override
    public CommentDTO addComment(CommentDTO commentDTO, String postid, String userid) throws ExecutionException, InterruptedException {
        System.out.println("User ID: " + userid);
        System.out.println("post ID: " + postid);
        User user = firebaseService.getDocument("users", userid, User.class);
        Post post = firebaseService.getDocument("posts", postid, Post.class);
        if (user == null) {
            throw new ResourceNotFoundException("User with id: " + userid + " not found");
        }
        if (post == null) {
            throw new ResourceNotFoundException("Post with id: " + postid + " not found");
        }
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setUser(user);
        comment.setPost(post);
       System.out.println(comment.getId());
        String updateTime = firebaseService.saveDocument("comments",comment.getId(), comment);
        System.out.println("Comment saved successfully with update time: " + updateTime);
        return this.modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public String deleteComment(String comment_id) {
        return (firebaseService.deleteDocument("comments", comment_id));
    }
}
