package com.blogapp.bloggingapplication.Services;

import com.blogapp.bloggingapplication.payloads.CommentDTO;

import java.util.concurrent.ExecutionException;

public interface CommentService {
    CommentDTO addComment(CommentDTO commentDTO,String post_id,String user_id)throws ExecutionException, InterruptedException ;
   String deleteComment(String comment_id);
}
